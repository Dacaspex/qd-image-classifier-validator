package gui;

import app.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Window class.
 * <p>
 * Window container for the validation tool
 */
public class Window extends JFrame implements KeyListener {

    /**
     * Reference to the application
     */
    private Application application;
    /**
     * Canvas on which the user can draw
     */
    private Canvas canvas;
    /**
     * Table frame that shows the results from the network
     */
    private ResultTable resultTable;

    public Window(Application application) {
        this.application = application;
        this.canvas = new Canvas();
        this.resultTable = new ResultTable();

        this.setPreferredSize(new Dimension(500, 500));
        this.add(canvas);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.addKeyListener(this);
    }

    /**
     * Transforms the image into a 28 by 28 image. It also boosts the
     * black colours a bit to make the image the same as the data set.
     *
     * @param image Original image
     * @return Resized image
     */
    public BufferedImage transformImage(BufferedImage image) {
        // Scale image to desired dimension (28 x 28)
        Image tmp = image.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);

        // Loop through each pixel of the new image
        for (int x = 0; x < 28; x++) {
            for (int y = 0; y < 28; y++) {
                // Get original color
                Color color = new Color(scaledImage.getRGB(x, y));

                // Ignore white values
                if (color.getRGB() == -1) {
                    continue;
                }

                // 'Boost' the grey values so they become more black
                float[] hsv = new float[3];
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
                hsv[2] = (float) 0.7 * hsv[2];
                int newColor = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);

                // Save new color
                scaledImage.setRGB(x, y, newColor);
            }
        }

        // Free resources
        g2d.dispose();

        return scaledImage;
    }

    /**
     * Grabs the image from the canvas object, transforms it and save to disk
     */
    public void export() {
        BufferedImage image = transformImage(canvas.getImage());

        // Save image
        try {
            File outputFile = new File("exported_image.png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the image against the network
     */
    public void test() {
        // Get data and sort it
        BufferedImage image = transformImage(canvas.getImage());
        Map<String, Float> result = application.getResult(image)
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Setup array
        String[][] data = new String[result.size()][2];
        int index = result.size() - 1;

        // Iterate over result set and put into table data array
        Iterator iter = result.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            data[index][0] = (String) entry.getKey();
            data[index--][1] = Float.toString((float) entry.getValue());
        }

        // Show data
        resultTable.setData(data);
        resultTable.open();
        this.requestFocus();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case KeyEvent.VK_ENTER:
                test();
                break;
            case KeyEvent.VK_BACK_SPACE:
                System.out.println("Clearing screen...");
                canvas.clear();
                break;

            case KeyEvent.VK_E:
                System.out.println("Exporting...");
                export();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
