package src.main.controller;

import src.main.model.ImageModel;
import src.main.model.ColorConverter;
import src.main.view.ImageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ImageController {
    private ImageModel model;
    private ImageView view;

    public ImageController(ImageModel model, ImageView view) {
        this.model = model;
        this.view = view;

        view.getLoadButton().addActionListener(e -> loadImage());
        view.addImageClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPixelColor(e.getX(), e.getY());
            }
        });
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(view.getLoadButton());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                model.setImage(ImageIO.read(file));
                view.setImage(new ImageIcon(model.getImage()));
                updateImageInfo(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view.getLoadButton(), "Ошибка загрузки изображения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateImageInfo(File file) {
        String info = String.format(
                "Размер: %dx%d, Глубина цвета: %d бит",
                model.getWidth(), model.getHeight(), model.getColorDepth()
        );
        view.setInfoText(info);
    }

    private void showPixelColor(int x, int y) {
        Color color = model.getPixelColor(x, y);
        if (color != null) {
            String rgbInfo = String.format("RGB: (%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue());
            String cmykInfo = ColorConverter.rgbToCmyk(color);
            String hslInfo = ColorConverter.rgbToHsl(color);
            String hsvInfo = ColorConverter.rgbToHsv(color);
            String ycbcrInfo = ColorConverter.rgbToYcbcr(color);

            String colorInfo = String.format(
                    "%s | CMYK: %s | HSL: %s | HSV: %s | YCbCr: %s",
                    rgbInfo, cmykInfo, hslInfo, hsvInfo, ycbcrInfo
            );
            view.setColorText(colorInfo);
        }
    }
}