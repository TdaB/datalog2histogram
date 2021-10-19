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
    private final String delimiter;
    private List<String> columnNames = new ArrayList<>();
    private Map<Point, CellData> table;
    private Set<Double> xValues = new TreeSet<>();
    private Set<Double> yValues = new TreeSet<>(Comparator.reverseOrder());
    private double xWidth = config.getDouble("table.axis.xwidth");;
    private double yWidth = config.getDouble("table.axis.ywidth");;

    public CSVParser(String filePath) throws Exception {
        this(filePath, "\t");
    }

    public CSVParser(String filePath, String delimiter) throws Exception {
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("File path was not provided to CSV parser");
        }
        this.filePath = filePath;
        this.delimiter = delimiter;
        this.parseHeader();
        this.getAxisData();
    }
    
    private void getAxisData() {
        double xMin = config.getDouble("table.axis.xmin");
        double xMax = config.getDouble("table.axis.xmax");
        double yMin = config.getDouble("table.axis.ymin");
        double yMax = config.getDouble("table.axis.ymax");

        for (double x = xMin; x <= xMax; x += this.xWidth) {
            this.xValues.add(x);
        }

        for (double y = yMin; y <= yMax; y += this.yWidth) {
            this.yValues.add(y);
        }
    }

    private void parseHeader() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(this.delimiter);
                if (parts.length < 3) {
                    throw new RuntimeException("Not enough columns provided in CSV: " + parts.length);
                }
                this.columnNames.addAll(Arrays.asList(parts));
                return;
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

    protected void parseCsv(String xAxis, String yAxis, String zAxis, Double neutral, Double high, Double low) throws Exception {
        this.table = new HashMap<>();
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
                    if (Math.abs(x - val) <= this.xWidth / 2) {
                        binnedX = val;
                        xFrac = 1 - (Math.abs(x - val) / (this.xWidth / 2));
                        break;
                    }
                }
                double binnedY = 0;
                double yFrac = 0;
                for (double val : this.getyValues()) {
                    if (Math.abs(y - val) <= this.yWidth / 2) {
                        binnedY = val;
                        yFrac = 1 - (Math.abs(y - val) / (this.yWidth / 2));
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
