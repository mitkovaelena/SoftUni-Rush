package dataHandler;

import java.util.Random;

/**
 * Under Construction.
 */
public class GetRandomToken extends Sprite {

    private String imagesDirectory;

    public GetRandomToken( String[] tokens) {

        setImagesDirectory(tokens);
    }

    protected String getImagesDirectory() {
        return this.imagesDirectory;
    }

    private void setImagesDirectory(String[] tokens) {

        String random = (tokens[new Random().nextInt(tokens.length)]);
        this.imagesDirectory = Constants.IMAGES_PATH + random + ".png";

    }
}