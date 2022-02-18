package com.refinery408;

import java.io.BufferedReader;
import java.io.File;
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
    private final File file;
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
        this.file = new File(filePath);
        this.parseHeader();
    }

    public CSVParser(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new RuntimeException("File was not provided to CSV parser");
        }
        this.file = file;
        this.parseHeader();
    }

    private void parseHeader() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            for (String line; (line = br.readLine()) != null; ) {
                for (String delimiter : DELIMITERS) {
                    String[] parts = line.split(delimiter);
                    if (parts.length >= 3) {
                        this.delimiter = delimiter;
                        Arrays.asList(parts)
                              .forEach(h -> this.columnNames.add(h.replaceAll("^\"|\"$", "")));
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

    public String getFileName() {
        return this.file.getName();
    }

    protected void parseCsv(TableConfig tableConfig) throws Exception {
        this.table = new HashMap<>();
        this.xValues = new TreeSet<>();
        this.yValues = new TreeSet<>(Comparator.reverseOrder());

        for (double x = tableConfig.getXMin(); x <= tableConfig.getXMax(); x += tableConfig.getXSpacing()) {
            this.xValues.add(x);
        }
        for (double y = tableConfig.getYMin(); y <= tableConfig.getYMax(); y += tableConfig.getYSpacing()) {
            this.yValues.add(y);
        }

        int xIndex = this.columnNames.indexOf(tableConfig.getXLabel());
        int yIndex = this.columnNames.indexOf(tableConfig.getYLabel());
        int zIndex = this.columnNames.indexOf(tableConfig.getZLabel());

        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
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

                Double binnedX = null;
                double xFrac = 0;
                for (double val : this.getxValues()) {
                    if (Math.abs(x - val) <= .5 * tableConfig.getXSpacing()) {
                        binnedX = val;
                        xFrac = 1 - (Math.abs(x - val) / (.5 * tableConfig.getXSpacing()));
                        break;
                    }
                }
                if (binnedX == null) {
                    continue;
                }

                Double binnedY = null;
                double yFrac = 0;
                for (double val : this.getyValues()) {
                    if (Math.abs(y - val) <= .5 * tableConfig.getYSpacing()) {
                        binnedY = val;
                        yFrac = 1 - (Math.abs(y - val) / (.5 * tableConfig.getYSpacing()));
                        break;
                    }
                }
                if (binnedY == null) {
                    continue;
                }

                double frac = .5 * xFrac + .5 * yFrac;
                Point p = new Point(binnedX, binnedY);
                if (this.table.containsKey(p)) {
                    this.table.get(p).updateAverage(z, frac);
                } else {
                    CellData data = new CellData(tableConfig.getZNeutral(),
                                                 tableConfig.getZHigh(),
                                                 tableConfig.getZLow());
                    data.updateAverage(z, frac);
                    this.table.put(p, data);
                }
            }
        }
    }
}
