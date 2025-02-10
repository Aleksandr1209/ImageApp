package src.main.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageModel {
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        return image != null ? image.getWidth() : 0;
    }

    public int getHeight() {
        return image != null ? image.getHeight() : 0;
    }

    public int getColorDepth() {
        return image != null ? image.getColorModel().getPixelSize() : 0;
    }

    public Color getPixelColor(int x, int y) {
        if (image != null && x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            int rgb = image.getRGB(x, y);
            return new Color(rgb);
        }
        return null;
    }
}