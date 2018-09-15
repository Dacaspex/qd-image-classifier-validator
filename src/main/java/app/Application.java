package app;

import gui.Window;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Application class responsible for managing the application state.
 */
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private Window window;
    private MultiLayerNetwork model;

    public Application() throws Exception {
        this.loadModel();
        this.window = new Window(this);
    }

    public void getResult(BufferedImage image) {
        INDArray imageData = imageToNDArray(image);

        logger.info("Evaluating");
        INDArray output = model.output(imageData);

        logger.info(output.toString());
        logger.info("[alarm_clock, anvil, apple, car, lightning]");
    }

    public INDArray imageToNDArray(BufferedImage image) {
        float[] data = new float[image.getWidth() * image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                float grey = (float) (red + green + blue) / 3;
                float normalised = grey / 255.0f;

                data[y * image.getHeight() + x] = normalised;
            }
        }

        return Nd4j.create(data);
    }

    public void loadModel() throws Exception {
        // Load our model
        logger.info("*** Load model ***");
        File saveLocation = new File("trained_qd_model.zip");
        model = ModelSerializer.restoreMultiLayerNetwork(saveLocation);
    }
}
