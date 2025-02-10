package src.main.model;

import java.awt.Color;

public class ColorConverter {
    public static String rgbToCmyk(Color color) {
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;

        double k = 1 - Math.max(r, Math.max(g, b));
        double c = (1 - r - k) / (1 - k);
        double m = (1 - g - k) / (1 - k);
        double y = (1 - b - k) / (1 - k);

        return String.format("(%.2f, %.2f, %.2f, %.2f)", c, m, y, k);
    }

    public static String rgbToHsl(Color color) {
        float[] hsl = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsl);
        return String.format("(%.2f, %.2f, %.2f)", hsl[0], hsl[1], hsl[2]);
    }

    public static String rgbToHsv(Color color) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
        return String.format("(%.2f, %.2f, %.2f)", hsv[0], hsv[1], hsv[2]);
    }

    public static String rgbToYcbcr(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        double y = 0.299 * r + 0.587 * g + 0.114 * b;
        double cb = 128 - 0.168736 * r - 0.331264 * g + 0.5 * b;
        double cr = 128 + 0.5 * r - 0.418688 * g - 0.081312 * b;

        return String.format("(%.2f, %.2f, %.2f)", y, cb, cr);
    }
}