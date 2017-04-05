package gameEngine;

import constants.CarConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import models.sprites.Collectible;
import models.sprites.Obstacle;
import models.Player;
import music.MusicPlayer;

import java.util.List;

public class PauseHandler {
    private Timeline gameLoop;
    private GraphicsContext gc;
    private Image background;
    private double y;
    private Player player;
    private List<Obstacle> testObstacles;
    private List<Collectible> collectibles;

    public PauseHandler(Timeline gameLoop, GraphicsContext gc, Image background, double y, Player player, List<Obstacle> testObstacles, List<Collectible> collectibles) {
        this.gameLoop = gameLoop;
        this.gc = gc;
        this.background = background;
        this.y = y;
        this.player = player;
        this.testObstacles = testObstacles;
        this.collectibles = collectibles;
    }

    public void activatePause() {
        RunTrack.setIsPaused(true);
        this.gameLoop.pause();
        if (RunTrack.isPaused()) {
            MusicPlayer.pause();

            Timeline pauseloop = new Timeline();
            pauseloop.setCycleCount(Timeline.INDEFINITE);

            KeyFrame keyFramePause = new KeyFrame(
                    Duration.seconds(CarConstants.FRAMES_PER_SECOND),
                    event -> {
                        if (!RunTrack.isPaused()) {
                            gameLoop.play();
                            MusicPlayer.pause();
                            pauseloop.stop();
                        }

                        gc.clearRect(0, 0, CarConstants.CANVAS_WIDTH, CarConstants.CANVAS_HEIGHT);
                        gc.drawImage(background, 0, y);
                        gc.drawImage(background, 0, y - CarConstants.CANVAS_HEIGHT);
                        this.player.getCar().render(gc);

                        for (Collectible collectible : collectibles) {
                            collectible.render(gc);
                        }
                        for (Obstacle obs : testObstacles) {
                            obs.render(gc);
                        }
                    });
            pauseloop.getKeyFrames().add(keyFramePause);
            pauseloop.play();
        }
    }
}