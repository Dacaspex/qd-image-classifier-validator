package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * The user can use his mouse to draw on the canvas
 */
public class Canvas extends JPanel implements MouseMotionListener, MouseListener {

    /**
     * All paths the user drew
     */
    private List<List<Point>> paths;
    /**
     * The current drawing path of the user
     */
    private List<Point> currentPath;
    /**
     * Canvas image which stores what is drawn on it
     */
    private BufferedImage image;
    /**
     * Graphics object of the image
     */
    private Graphics2D gImage;
    /**
     * Brush size of the pencil with which the user draws
     */
    private int brushSize;

    public Canvas() {
        this.paths = new ArrayList<List<Point>>();
        this.currentPath = new ArrayList<Point>();
        this.brushSize = 17;
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * @return The drawn image
     */
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create image if it has not been created yet
        if (image == null) {
            clear();
        }

        // Set color and stroke
        gImage.setColor(Color.BLACK);
        gImage.setStroke(new BasicStroke(brushSize));

        // Draw the paths on the image
        for (List<Point> path : paths) {
            Point from = null;
            for (Point p : path) {
                if (from != null) {
                    gImage.drawLine(from.x, from.y, p.x, p.y);
                }
                from = p;
            }
        }

        g.drawImage(image, 0, 0, null);
    }

    /**
     * Clears the image
     */
    public void clear() {
        // Create image
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // Get graphics context and clear image
        gImage = (Graphics2D) image.getGraphics();
        gImage.setColor(Color.WHITE);
        gImage.fillRect(0, 0, getWidth(), getHeight());

        // Clear paths
        paths = new ArrayList<List<Point>>();
        currentPath = new ArrayList<Point>();

        // Repaint to show effects
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        currentPath.add(e.getPoint());
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        currentPath = new ArrayList<Point>();
        currentPath.add(e.getPoint());

        paths.add(currentPath);
    }

    public void mouseReleased(MouseEvent e) {
        currentPath = null;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
