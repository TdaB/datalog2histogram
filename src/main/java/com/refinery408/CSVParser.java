package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CSVParser {
    private static final Logger log = LoggerFactory.getLogger(CSVParser.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private final String filePath;
    private String delimiter;
    private List<String> columnNames = new ArrayList<>();
    private Map<Point, CellData> table;
    private Set<Double> xValues = new TreeSet<>();
    private Set<Double> yValues = new TreeSet<>(Comparator.reverseOrder());
    private static final List<String> DELIMITERS = Arrays.asList("\t", ",", ";", " ");

    public CSVParser(String filePath) throws Exception {
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("File path was not provided to CSV parser");
        }
        this.filePath = filePath;
        this.parseHeader();
    }

    private void parseHeader() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            for (String line; (line = br.readLine()) != null; ) {
                for (String delimiter : DELIMITERS) {
                    String[] parts = line.split(delimiter);
                    if (parts.length >= 3) {
                        this.delimiter = delimiter;
                        this.columnNames.addAll(Arrays.asList(parts));
                        return;
                    }
                }
                throw new RuntimeException("Failed to parse CSV with <TAB>, <COMMA>, <SEMICOLON>, and <SPACE>");
            }
        }
    }

    public Map<Point, CellData> getData() {
        return table;
    }

    public Set<Double> getxValues() {
        return xValues;
    }

    public Set<Double> getyValues() {
        return yValues;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    protected void parseCsv(String xAxis,
                            String yAxis,
                            String zAxis,
                            Double neutral,
                            Double high,
                            Double low,
                            Double xMin,
                            Double xMax,
                            int xSpacing,
                            Double yMin,
                            Double yMax,
                            int ySpacing) throws Exception {
        this.table = new HashMap<>();
        this.xValues = new TreeSet<>();
        this.yValues = new TreeSet<>(Comparator.reverseOrder());

        for (double x = xMin; x <= xMax; x += xSpacing) {
            this.xValues.add(x);
        }
        for (double y = yMin; y <= yMax; y += ySpacing) {
            this.yValues.add(y);
        }

        int xIndex = this.columnNames.indexOf(xAxis);
        int yIndex = this.columnNames.indexOf(yAxis);
        int zIndex = this.columnNames.indexOf(zAxis);

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            boolean firstLine = true;
            for (String line; (line = br.readLine()) != null; ) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(this.delimiter);
                double x = Double.parseDouble(parts[xIndex]);
                double y = Double.parseDouble(parts[yIndex]);
                double z = Double.parseDouble(parts[zIndex]);

                double binnedX = 0;
                double xFrac = 0;
                for (double val : this.getxValues()) {
                    if (Math.abs(x - val) <= .5 * xSpacing) {
                        binnedX = val;
                        xFrac = 1 - (Math.abs(x - val) / (.5 * xSpacing));
                        break;
                    }
                }
                double binnedY = 0;
                double yFrac = 0;
                for (double val : this.getyValues()) {
                    if (Math.abs(y - val) <= .5 * ySpacing) {
                        binnedY = val;
                        yFrac = 1 - (Math.abs(y - val) / (.5 * ySpacing));
                        break;
                    }
                }
                double frac = .5 * xFrac + .5 * yFrac;
                Point p = new Point(binnedX, binnedY);
                if (this.table.containsKey(p)) {
                    this.table.get(p).updateAverage(z, frac);
                } else {
                    CellData data = new CellData(neutral, high, low);
                    data.updateAverage(z, frac);
                    this.table.put(p, data);
                }
            }
        }
    }
}
