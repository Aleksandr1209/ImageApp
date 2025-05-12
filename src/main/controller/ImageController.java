package src.main.controller;

import src.main.model.ColorConverter;
import src.main.model.ImageModel;
import src.main.view.ImageView;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageController {
    private final ImageModel model;
    private final ImageView view;

    public ImageController(ImageModel model, ImageView view) {
        this.model = model;
        this.view = view;
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getLoadButton().addActionListener(e -> loadImage());

        view.addImageClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (model.getImage() == null) {
                    view.setColorText("No image loaded");
                    return;
                }

                Point imgPoint = view.getImageCoordinates(e.getPoint());

                if (imgPoint == null) {
                    view.setColorText("Clicked outside image");
                    return;
                }

                showPixelColor(imgPoint.x, imgPoint.y);
            }
        });
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);

                if (image == null) {
                    throw new IOException("Unsupported image format");
                }

                model.setImage(image);
                view.setImage(new ImageIcon(image));
                updateImageInfo(file);
                view.setColorText("Image loaded successfully");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error loading image: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showPixelColor(int x, int y) {
        try {
            if (x < 0 || y < 0 || x >= model.getWidth() || y >= model.getHeight()) {
                view.setColorText("Invalid coordinates");
                return;
            }

            Color color = model.getPixelColor(x, y);
            String colorInfo = String.format(
                    "<html><b>Color at (%d,%d):</b><br>"
                            + "RGB: (%d, %d, %d)<br>"
                            + "HSV: %s<br>"
                            + "HSL: %s<br>"
                            + "YCbCr: %s<br>"
                            + "CMYK: %s<br>"
                            + "LAB: %s</html>",
                    x, y,
                    color.getRed(), color.getGreen(), color.getBlue(),
                    ColorConverter.rgbToHsv(color),
                    ColorConverter.rgbToHsl(color),
                    ColorConverter.rgbToYcbcr(color),
                    ColorConverter.rgbToCmyk(color),
                    ColorConverter.rgbToLab(color)
            );
            view.setColorText(colorInfo);

        } catch (Exception ex) {
            view.setColorText("Error calculating color values");
        }
    }

    private void updateImageInfo(File file) {
        try {
            String format = getFileExtension(file).toUpperCase();
            long sizeKB = file.length() / 1024;
            String info = String.format(
                    "<html><b>Image Info:</b><br>"
                            + "Format: %s<br>"
                            + "Size: %dx%d pixels<br>"
                            + "Color depth: %d bits<br>"
                            + "File size: %d KB</html>",
                    format,
                    model.getWidth(),
                    model.getHeight(),
                    model.getColorDepth(),
                    sizeKB
            );
            view.setInfoText(info);

        } catch (Exception ex) {
            view.setInfoText("Error reading image info");
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(lastDot + 1) : "Unknown";
    }
}