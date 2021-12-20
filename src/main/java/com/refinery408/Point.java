package com.refinery408;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Point {
    private final double x;
    private final double y;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point p = (Point) obj;

        return Objects.equals(this.x, p.x) &&
               Objects.equals(this.y, p.y);
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(this.x);
        bits ^= Double.doubleToLongBits(this.y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    @Override
    public String toString() {
        return String.format("X = %.2f, Y = %.2f", this.x, this.y);
    }
}
