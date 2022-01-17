package com.refinery408;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TableConfig {
    private final String xLabel;
    private final Double xMin;
    private final Double xMax;
    private final Double xSpacing;
    private final String yLabel;
    private final Double yMin;
    private final Double yMax;
    private final Double ySpacing;
    private final String zLabel;
    private final Double zNeutral;
    private final Double zHigh;
    private final Double zLow;
    private final Integer minHits;
}
