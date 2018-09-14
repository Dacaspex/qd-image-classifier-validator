package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame implements KeyListener {

    private Canvas canvas;

    public Window() {
        this.canvas = new Canvas();

        this.setPreferredSize(new Dimension(500, 500));
        this.add(canvas);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.addKeyListener(this);
    }

    /**
     * TODO: Clean up method
     * <p>
     * Grabs the image from the canvas object, transforms it and saves the final image.
     */
    public void export() {
        // Get the image from the canvas
        BufferedImage image = canvas.getImage();

        // Scale image to desired dimension (28 x 28)
        Image tmp = image.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);

        // Boost pixel values to become more black
        for (int x = 0; x < 28; x++) {
            for (int y = 0; y < 28; y++) {
                Color color = new Color(scaledImage.getRGB(x, y));

                if (color.getRGB() == -1) {
                    continue;
                }

                float[] hsv = new float[3];
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
                hsv[2] = (float) 0.7 * hsv[2];
                int newColor = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);

                scaledImage.setRGB(x, y, newColor);
            }
        }

        // Free resources
        g2d.dispose();

        // Save image
        try {
            File outputFile = new File("saved2.png");
            ImageIO.write(scaledImage, "png", outputFile);
        } catch (IOException e) {
            // handle exception
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case KeyEvent.VK_ENTER:
                System.out.println("Exporting...");
                export();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
