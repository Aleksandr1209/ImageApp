package src.main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class ImageView {
    private static final double MIN_SCALE = 0.1;
    private static final double MAX_SCALE = 10.0;
    private static final double ZOOM_FACTOR = 1.1;

    private JFrame frame;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel colorLabel;
    private JButton loadButton;
    private JScrollPane scrollPane;

    private double scale = 1.0;
    private BufferedImage originalImage;

    public ImageView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        imageLabel = new JLabel() {
            @Override
            public Dimension getPreferredSize() {
                if (originalImage == null) {
                    return new Dimension(0, 0);
                }
                return new Dimension(
                        (int)(originalImage.getWidth() * scale),
                        (int)(originalImage.getHeight() * scale)
                );
            }
        };
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        scrollPane = new JScrollPane(imageLabel);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        frame.add(scrollPane, BorderLayout.CENTER);

        infoLabel = new JLabel("Image info will appear here");
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(infoLabel, BorderLayout.NORTH);

        colorLabel = new JLabel("Pixel color will appear here");
        colorLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(colorLabel, BorderLayout.SOUTH);

        loadButton = new JButton("Load Image");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loadButton);
        frame.add(buttonPanel, BorderLayout.WEST);

        imageLabel.addMouseWheelListener(e -> {
            if (originalImage == null) return;

            int rotation = e.getWheelRotation();
            Point mousePos = e.getPoint();

            double zoom = (rotation < 0) ? ZOOM_FACTOR : 1/ZOOM_FACTOR;

            applySmoothZoom(zoom, mousePos);
        });

        frame.setVisible(true);
    }

    private void applySmoothZoom(double zoom, Point mousePos) {
        double oldScale = scale;
        scale = Math.min(MAX_SCALE, Math.max(MIN_SCALE, scale * zoom));

        if (scale != oldScale) {
            Point viewPos = scrollPane.getViewport().getViewPosition();
            viewPos.x = (int)(mousePos.x * scale / oldScale - mousePos.x + viewPos.x * scale / oldScale);
            viewPos.y = (int)(mousePos.y * scale / oldScale - mousePos.y + viewPos.y * scale / oldScale);

            updateScaledImage();

            SwingUtilities.invokeLater(() -> {
                scrollPane.getViewport().setViewPosition(viewPos);
            });
        }
    }

    private void updateScaledImage() {
        if (originalImage != null) {
            Image scaled = originalImage.getScaledInstance(
                    (int)(originalImage.getWidth() * scale),
                    (int)(originalImage.getHeight() * scale),
                    Image.SCALE_SMOOTH
            );
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.revalidate();
        }
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public void setImage(BufferedImage image) {
        if (image != null) {
            this.originalImage = image;
            this.scale = 1.0;
            updateScaledImage();
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        }
    }

    public void setImage(ImageIcon icon) {
        if (icon != null) {
            Image img = icon.getImage();
            if (img instanceof BufferedImage) {
                this.originalImage = (BufferedImage) img;
            } else {
                this.originalImage = new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = this.originalImage.createGraphics();
                g2d.drawImage(img, 0, 0, null);
                g2d.dispose();
            }
            this.scale = 1.0;
            updateScaledImage();
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        }
    }

    public void setInfoText(String text) {
        infoLabel.setText("<html><b>" + text + "</b></html>");
    }

    public void setColorText(String text) {
        colorLabel.setText("<html>" + text.replace(" | ", "<br>") + "</html>");
    }

    public void addImageClickListener(MouseAdapter listener) {
        imageLabel.addMouseListener(listener);
    }

    public Point getImageCoordinates(Point mousePoint) {
        if (originalImage == null) return null;

        Rectangle bounds = imageLabel.getBounds();
        bounds.x = bounds.y = 0;

        if (!bounds.contains(mousePoint)) {
            return null;
        }

        int x = (int)(mousePoint.x / scale);
        int y = (int)(mousePoint.y / scale);

        if (x >= 0 && y >= 0 && x < originalImage.getWidth() && y < originalImage.getHeight()) {
            return new Point(x, y);
        }
        return null;
    }
}