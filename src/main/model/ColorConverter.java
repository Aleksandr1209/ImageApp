package src.main.model;

import java.awt.Color;

public class ColorConverter {
    public static String rgbToCmyk(Color color) {
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;

        double k = 1 - Math.max(r, Math.max(g, b));
        double c = (k == 1) ? 0 : (1 - r - k) / (1 - k);
        double m = (k == 1) ? 0 : (1 - g - k) / (1 - k);
        double y = (k == 1) ? 0 : (1 - b - k) / (1 - k);

        return String.format("(%.2f, %.2f, %.2f, %.2f)", c, m, y, k);
    }

    public static String rgbToHsv(Color color) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
        return String.format("(H:%.1f°, S:%.1f%%, V:%.1f%%)",
                hsv[0] * 360, hsv[1] * 100, hsv[2] * 100);
    }

    public static String rgbToYcbcr(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        double y = 0.299 * r + 0.587 * g + 0.114 * b;
        double cb = 128 - 0.168736 * r - 0.331264 * g + 0.5 * b;
        double cr = 128 + 0.5 * r - 0.418688 * g - 0.081312 * b;

        return String.format("(Y:%.1f, Cb:%.1f, Cr:%.1f)", y, cb, cr);
    }

    public static String rgbToLab(Color color) {
        // Нормализация RGB [0-255] -> [0-1]
        double r = color.getRed() / 255.0;
        double g = color.getGreen() / 255.0;
        double b = color.getBlue() / 255.0;

        // Преобразование RGB -> XYZ (с учетом гамма-коррекции)
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // Матричное преобразование RGB -> XYZ
        double x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750;
        double z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041;

        // Нормализация XYZ относительно D65 (стандартный источник света)
        double xn = 95.047;
        double yn = 100.000;
        double zn = 108.883;

        x = x / xn;
        y = y / yn;
        z = z / zn;

        // Преобразование XYZ -> LAB
        x = (x > 0.008856) ? Math.pow(x, 1.0/3.0) : (7.787 * x) + (16.0/116.0);
        y = (y > 0.008856) ? Math.pow(y, 1.0/3.0) : (7.787 * y) + (16.0/116.0);
        z = (z > 0.008856) ? Math.pow(z, 1.0/3.0) : (7.787 * z) + (16.0/116.0);

        double l = (116.0 * y) - 16.0;
        double a = 500.0 * (x - y);
        double labB = 200.0 * (y - z);

        return String.format("(%.2f, %.2f, %.2f)", l, a, labB);
    }

    public static String rgbToHsl(Color color) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);

        // Конвертация HSV в HSL
        float l = (2 - hsv[1]) * hsv[2] / 2;
        float s = hsv[1] * hsv[2] / (l < 0.5 ? l * 2 : 2 - l * 2);

        return String.format("(H:%.1f°, S:%.1f%%, L:%.1f%%)",
                hsv[0] * 360,
                s * 100,
                l * 100);
    }
}