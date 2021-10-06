package com.refinery408;

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
    private final String filePath;
    private List<String> columns = new ArrayList<>();
    private Map<Point, CellData> table;
    private Set<Double> xValues = new TreeSet<>();;
    private Set<Double> yValues = new TreeSet<>(Comparator.reverseOrder());

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
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    throw new RuntimeException("Not enough columns provided in CSV: " + parts.length);
                }
                this.columns.addAll(Arrays.asList(parts));
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

    public List<String> getColumns() {
        return columns;
    }

    protected void parseCsv(String xAxis, String yAxis, String zAxis, Double neutral, Double high, Double low) throws Exception {
        this.table = new HashMap<>();
        this.xValues = new TreeSet<>();
        this.yValues = new TreeSet<>(Comparator.reverseOrder());
        int xIndex = this.columns.indexOf(xAxis);
        int yIndex = this.columns.indexOf(yAxis);
        int zIndex = this.columns.indexOf(zAxis);
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            boolean firstLine = true;
            for (String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(",");
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                double x = Double.parseDouble(parts[xIndex]);
                double y = Double.parseDouble(parts[yIndex]);
                double z = Double.parseDouble(parts[zIndex]);
                this.xValues.add(x);
                this.yValues.add(y);
                Point p = new Point(x, y);
                if (this.table.containsKey(p)) {
                    this.table.get(p).updateAverage(z);
                } else {
                    CellData data = new CellData(neutral, high, low);
                    data.updateAverage(z);
                    this.table.put(p, data);
                }
            }
        }
    }
}
