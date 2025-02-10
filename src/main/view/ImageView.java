package src.main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ImageView {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel colorLabel;
    private JButton loadButton;

    public ImageView() {
        frame = new JFrame("Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);

        infoLabel = new JLabel("Информация об изображении будет здесь.");
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(infoLabel, BorderLayout.NORTH);

        colorLabel = new JLabel("Цвет пикселя будет здесь.");
        colorLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(colorLabel, BorderLayout.SOUTH);

        loadButton = new JButton("Загрузить изображение");
        frame.add(loadButton, BorderLayout.WEST);

        frame.setVisible(true);
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public void setImage(ImageIcon icon) {
        imageLabel.setIcon(icon);
    }

    public void setInfoText(String text) {
        infoLabel.setText(text);
    }

    public void setColorText(String text) {
        colorLabel.setText(text);
    }

    public void addImageClickListener(MouseAdapter listener) {
        imageLabel.addMouseListener(listener);
    }
}