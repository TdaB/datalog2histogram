package com.refinery408;

import java.awt.Color;

import static java.lang.Math.pow;

public class ColorUtils {
    public static Color invert(Color c) {
        if (c == null) return null;
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    public static Color interpolate(Color c1, Color c2, float fraction) {
        if (fraction <= 0) {
            return c1;
        }
        if (fraction >= 1) {
            return c2;
        }

        float startA = c1.getAlpha() / 255.0f;
        float startR = c1.getRed() / 255.0f;
        float startG = c1.getGreen() / 255.0f;
        float startB = c1.getBlue() / 255.0f;

        float endA = c2.getAlpha() / 255.0f;
        float endR = c2.getRed() / 255.0f;
        float endG = c2.getGreen() / 255.0f;
        float endB = c2.getBlue() / 255.0f;

        // convert from sRGB to linear
        startR = EOCF_sRGB(startR);
        startG = EOCF_sRGB(startG);
        startB = EOCF_sRGB(startB);

        endR = EOCF_sRGB(endR);
        endG = EOCF_sRGB(endG);
        endB = EOCF_sRGB(endB);

        // compute the interpolated color in linear space
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // convert back to sRGB in the [0..255] range
        r = OECF_sRGB(r);
        g = OECF_sRGB(g);
        b = OECF_sRGB(b);

        return new Color(r, g, b, a);
    }

    /**
     * Opto-electronic conversion function for the sRGB color space
     * Takes a gamma-encoded sRGB value and converts it to a linear sRGB value
     */
    private static float OECF_sRGB(float linear) {
        // IEC 61966-2-1:1999
        return linear <= 0.0031308f ?
               linear * 12.92f : (float) ((pow(linear, 1.0f / 2.4f) * 1.055f) - 0.055f);
    }

    /**
     * Electro-optical conversion function for the sRGB color space
     * Takes a linear sRGB value and converts it to a gamma-encoded sRGB value
     */
    private static float EOCF_sRGB(float srgb) {
        // IEC 61966-2-1:1999
        return srgb <= 0.04045f ? srgb / 12.92f : (float) pow((srgb + 0.055f) / 1.055f, 2.4f);
    }
}
