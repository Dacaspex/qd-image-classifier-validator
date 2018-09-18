package app;

import gui.Window;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Application class responsible for managing the application state.
 */
public class Application {

    private Window window;
    private MultiLayerNetwork model;

    public Application() throws Exception {
        this.loadModel();
        this.window = new Window(this);
    }

    /**
     * Gets and prints the result of the network based on the image
     *
     * @param image Input image
     */
    public void getResult(BufferedImage image) {
        INDArray imageData = imageToNDArray(image);
        INDArray output = model.output(imageData);

        String[] labels = new String[]{
                "airplane",
                "alarm_clock      ",
                "apple      ",
                "baseball_bat        ",
                "books  ",
                "brain",
                "butterfly",
                "cactus",
                "cake",
                "dolphin",
                "flashlight",
                "flower",
                "flying_saucer",
                "golf_club",
                "hat",
                "lightning",
                "lobster",
                "mailbox",
                "matches",
                "mug",
                "octopus",
                "palm_tree",
                "panda",
                "penguin",
                "radio",
                "sandwich",
                "saw",
                "shark",
                "shoe",
                "submarine"
        };
//        String[] labels = new String[]{
//                "aircraft_carrier",
//                "baseball_bat",
//                "birthday_cake",
//                "bush",
//                "cat",
//                "crayon"
//        };

        for (int i = 0; i < output.length(); i++) {
            System.out.println(labels[i] + ": " + output.getColumn(i));
        }
    }

    /**
     * Converts a buffered image into a NDArray.
     *
     * @param image Input image
     * @return 1D NDArray containing the image data
     */
    public INDArray imageToNDArray(BufferedImage image) {
        float[][][][] data = new float[1][1][image.getWidth()][image.getHeight()];

        // Loop through each pixel of the image
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                // Get color components (r, g, b)
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                // Calculate grey scale value and normalise
                float grey = (float) (red + green + blue) / 3;
                float normalised = 1 - grey / 255.0f;

                // Save value into array
                data[0][0][y][x] = normalised;
            }
        }

        // Convert java array to NDArray
        return Nd4j.create(data);
    }

    /**
     * Loads the model
     *
     * @throws Exception If the model could not be laoded
     */
    public void loadModel() throws Exception {
        // Load our model
        System.out.println("Loading model...");
        File saveLocation = new File("trained_qd_model.zip");
        model = ModelSerializer.restoreMultiLayerNetwork(saveLocation);
    }
}
