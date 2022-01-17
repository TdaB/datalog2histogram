package com.refinery408;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.util.Map;
import java.util.Set;

public class HistogramTableModel extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(HistogramTableModel.class);
    private final Map<Point, CellData> data;
    private Set<Double> xLabels;
    private Set<Double> yLabels;
    private Double[][] averages;
    private Double[][] hitPercentages;
    private Integer[][] hits;
    private Color[][] colors;
    private int minHits;

    public HistogramTableModel(Map<Point, CellData> data, Set<Double> xLabels, Set<Double> yLabels, int minHits) {
        this.data = data;
        this.xLabels = xLabels;
        this.yLabels = yLabels;
        this.averages = new Double[this.yLabels.size()][this.xLabels.size()];
        this.hitPercentages = new Double[this.yLabels.size()][this.xLabels.size()];
        this.hits = new Integer[this.yLabels.size()][this.xLabels.size()];
        this.colors = new Color[this.yLabels.size()][this.xLabels.size()];
        this.minHits = minHits;
        this.populateMatrices();
    }

    @Override
    public int getRowCount() {
        return this.yLabels.size();
    }

    @Override
    public int getColumnCount() {
        return this.xLabels.size();
    }

    @Override
    public String getColumnName(int column) {
        return String.valueOf(this.xLabels.toArray()[column]);
    }

    @Override
    public Object getValueAt(int row, int col) {
        Double value = this.averages[row][col];
        Integer hits = this.hits[row][col];
        return value == null || hits < this.minHits ? null : String.format("%5.3f", value);
    }

    public Integer getHitsAt(int row, int col) {
        return this.hits[row][col];
    }

    public Double getHitPercentageAt(int row, int col) {
        return this.hitPercentages[row][col];
    }

    public Color[][] getColors() {
        return colors;
    }

    private void populateMatrices() {
        if (this.data == null || this.data.size() == 0) {
            return;
        }
        Double[] xLabels = this.xLabels.toArray(new Double[0]);
        Double[] yLabels = this.yLabels.toArray(new Double[0]);
        for (int row = 0; row < yLabels.length; row++) {
            for (int col = 0; col < xLabels.length; col++) {
                double xLabel = xLabels[col];
                double yLabel = yLabels[row];
                CellData data = this.data.get(new Point(xLabel, yLabel));
                if (data == null) {
                    averages[row][col] = null;
                    hitPercentages[row][col] = null;
                    hits[row][col] = 0;
                    colors[row][col] = Color.GRAY;
                    continue;
                }
                averages[row][col] = data.getAverage();
                hitPercentages[row][col] = data.getHitPercentage();
                hits[row][col] = data.getHits();
                colors[row][col] = data.getColor();
            }
        }
    }
}
