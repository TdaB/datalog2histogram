package com.refinery408;

import lombok.Getter;

import java.awt.Color;

@Getter
public class CellData {
    private final Double neutralValue;
    private final Double highValue;
    private final Double lowValue;
    private double average;
    private double hitPercentage;
    private int hits = 0;

    public CellData(Double neutralValue, Double highValue, Double lowValue) {
        this.neutralValue = neutralValue;
        this.highValue = highValue;
        this.lowValue = lowValue;
    }

    public void updateAverage(double value, double frac) {
        if (this.hits == 0) {
            this.average = frac * value;
            this.hitPercentage = frac;
        } else {
            this.average += frac * ((value - this.average) / (this.hits + 1));
            this.hitPercentage += ((frac - this.hitPercentage) / (this.hits + 1));
        }
        this.hits += 1;
    }

    @Override
    public String toString() {
        return String.format("Average = %.2f, Hits = %d", this.average, this.hits);
    }

    protected Color getColor() {
        if (this.highValue != null && this.average >= this.neutralValue) {
            float fraction = (float) ((this.average - this.neutralValue) / (this.highValue - this.neutralValue));
            return ColorUtils.interpolate(Color.GREEN, Color.RED, fraction);
        }
        if (this.lowValue != null && this.average <= this.neutralValue) {
            float fraction = (float) ((this.average - this.neutralValue) / (this.lowValue - this.neutralValue));
            return ColorUtils.interpolate(Color.GREEN, Color.BLUE, fraction);
        }
        return null;
    }
}
