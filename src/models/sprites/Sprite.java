package models.sprites;

import gameEngine.DrawImageInCanvas;
import interfaces.Updateable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.constants.ErrorConstants;
import utils.constants.GameplayConstants;
import utils.constants.GeneralConstants;

public abstract class Sprite implements Updateable{
    private int minLeftSide;
    private int maxRightSide;
    private String name;
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double imageWidth;
    private double imageHeight;

    protected Sprite() {
    }

    public boolean intersects(Sprite other) {
        return this.getBoundary().intersects(other.getBoundary());
    }

    protected int getMinLeftSide() {
        return minLeftSide;
    }

    protected int getMaxRightSide() {
        return maxRightSide;
    }

    public void render(GraphicsContext gc) {
        DrawImageInCanvas.drawImage(gc, this.getImage(), this.getPositionX(), this.getPositionY());
    }

    public void setVelocity(double x, double y) {
        this.setVelocityX(x);
        this.setVelocityY(y);
    }

    @Override
    public void update() {
        this.positionX += this.velocityX;
        this.positionY += this.velocityY;
    }

    public void addVelocity(double x, double y) {
        this.addVelocity(x, y, this.minLeftSide, this.maxRightSide);
    }

    protected void addVelocity(double x, double y, int min, int max) {
        this.minLeftSide = min;
        this.maxRightSide = max;

        if (x < 0) {
            if (this.positionX > min) {
                this.velocityX += x;
            }
        } else if (x > 0) {
            if (this.positionX < max) {
                this.velocityX += x;
            }
        }
        if (y < GameplayConstants.CANVAS_BEGINNING) {
            if (this.positionY > GameplayConstants.CANVAS_Y_END) {
                this.velocityY += y;
            }
        } else if (y > GameplayConstants.CANVAS_BEGINNING) {
            if (this.positionY < GeneralConstants.CANVAS_HEIGHT - this.imageHeight * GameplayConstants.IMAGE_HEIGHT_OFFSET) {
                this.velocityY += y;
            }
        }
    }

    public final String getName() {
        return this.name;
    }

    private void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ErrorConstants.NAME_EXCEPTION);
        }
        this.name = name;
    }

    public void updateName(String name) {
        this.setName(name);
    }

    public final Image getImage() {
        return this.image;
    }

    private void setImage(Image image) {
        this.image = image;
    }

    public void updateImage(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException(ErrorConstants.FILE_PATH_EXCEPTION);
        }
        Image image = new Image(filename);
        this.setImage(image);
        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
    }

    public void updatePosition(double x, double y) {
        this.setPosition(x, y);
    }

    public final double getImageHeight() {
        return this.imageHeight;
    }

    public void updateVelocityX(double velocityX) {
        this.setVelocityX(velocityX);
    }

    public void updateVelocityY(double velocityY) {
        this.setVelocityY(velocityY);
    }

    protected final double getPositionX() {
        return this.positionX;
    }

    protected final double getPositionY() {
        return this.positionY;
    }

    private void setPosition(double x, double y) {
        if (x < 0 || y < GameplayConstants.OBSTACLE_ANIMATION_Y_OFFSET) {
            throw new IllegalArgumentException(ErrorConstants.POSITION_EXCEPTION);
        }
        this.positionX = x;
        this.positionY = y;
    }

    private void setVelocityY(double velocityY) {
        if (velocityY < -5) {
            throw new IllegalArgumentException(ErrorConstants.VELOCITY_EXCEPTION);
        }
        this.velocityY = velocityY;
    }

    private void setVelocityX(double velocityX) {
        if (velocityX < GameplayConstants.X_TOP_RENDER_SPEED_DRUNK_DRIVERS) {
            throw new IllegalArgumentException(ErrorConstants.VELOCITY_EXCEPTION);
        }
        this.velocityX = velocityX;
    }

    private Rectangle2D getBoundary() {
        return new Rectangle2D(this.positionX, this.positionY, this.imageWidth, this.imageHeight);
    }
}