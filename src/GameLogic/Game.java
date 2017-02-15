package GameLogic;

import Controllers.ChooseCarController;
import Controllers.LoginController;
import Controllers.ScreenController;
import DataHandler.*;
import KeyHandler.KeyHandlerOnPress;
import KeyHandler.KeyHandlerOnRelease;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static Controllers.ScreenController.*;

public class Game {
    private static float velocity = 5;
    private static CurrentDistance currentDistance = new CurrentDistance(0);
    private static AnchorPane root = ScreenController.root;
    private static int frame = 0;
    private static long time = 0;
    private static boolean isPaused = false;
    private static double y;
    private static MediaPlayer mediaPlayer;
    private static ArrayList<Sprite> testObstacles = new ArrayList<>();
    private static ArrayList<Sprite> collectibles = new ArrayList<>();
    private static Player player = LoginController.player;
    private static String carId = ChooseCarController.carId;
    private static CurrentPoints currentPoints = new CurrentPoints(0);
    private static CurrentTime currentTime = new CurrentTime(0);
    private static HealthBar currentHealth;

    private static Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
        }
    };

    public static void RunTrack(Image background) {
        AnchorPane root = ScreenController.root;
        Canvas canvas = new Canvas(500, 600);
        //EventHandler<? super KeyEvent> onKeyPressed = root.getOnKeyPressed();
        if (ScreenController.gamePlayStage != null) {
            Stage stage = (Stage) canvas.getScene().getWindow();
            try {
                loadStage(stage, gamePlayStage, "../views/gameOver.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        root.getChildren().add(canvas);
        root.getScene().setOnKeyPressed(new KeyHandlerOnPress(player));
        root.getScene().setOnKeyReleased(new KeyHandlerOnRelease(player));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        carId = carId == null ? "car1" : carId;
        String carImg = "/resources/images/player_" + carId + ".png";
        player.setImage(carImg);
        //playerCar.setImage("/resources/images/player_car3.png");  depending on level?
        player.setPosition(200, 430);
        player.setPoints(0L);
        currentHealth = new HealthBar(player);

        currentPoints.addObserver(observer);
        currentTime.addObserver(observer);
        currentDistance.addObserver(observer);
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        playMusic();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        y = y + velocity ;
                        time++;
                        frame++;


                        currentTime.setValue((long) (time * 0.017));
                        currentDistance.setValue(currentDistance.getValue() + (long) velocity/2);
                        player.setPoints(player.getPoints()+1);
                        currentPoints.setValue(player.getPoints());

                        observer.update(currentPoints, observer);
                        observer.update(currentTime, observer);
                        observer.update(currentDistance, observer);
                        if (Math.abs(y) >= 600) {
                            y = y - 600;
                            frame = 0;
                        }
                        player.setVelocity(0, 0);

                        //Pause
                        if (isPaused) {
                            handleGamePause(gameLoop, gc, background);
                        }

                        //Generate obstacles
                        if (frame % 50000 == 0) {
                            testObstacles.add(Obstacle.generateObstacle());
                        }

                        gc.clearRect(0, 0, 500, 600);
                        gc.drawImage(background, 0, y - 600);
                        gc.drawImage(background, 0, y);
                        player.update();
                        player.render(gc);
                        currentHealth.render();
                        //observer.update(currentHealth, observer);
                        manageObstacles(gc);

                        if (player.getHealthPoints() <= 0) {
                            clearObstaclesAndCollectibles();
                            gameLoop.stop();
                            stopMusic();
                            time = 0;
                            player.setHealthPoints(100);
                            if (player.getHighScore() < player.getPoints()) {
                                player.setHighScore(player.getPoints());
                            }
                            player.setPoints(0L);
                            player.stopAccelerate();
                            velocity=5;
                            currentDistance.setValue(0);
                            Stage stage = (Stage) canvas.getScene().getWindow();
                            root.getChildren().remove(canvas);
                            try {
                                loadStage(stage, gameOverStage, "../views/gameOver.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (frame % 50000 == 0) {
                            collectibles.add(Collectible.generateCollectible());
                        }
                        visualizeCollectible(gc, velocity);
                    }
                });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.playFromStart();
    }

    private static void manageObstacles(GraphicsContext gc) {
        for (Sprite testObst : testObstacles) {
            if (testObst.getName().substring(0, 6).equals("player") && !testObst.isDestroyed()) {
                testObst.setVelocity(0, velocity / 2);
            } else {
                testObst.setVelocity(0, velocity);
            }
            testObst.update();
            testObst.render(gc);

            if (testObst.getBoundary().intersects(player.getBoundary())) {
                if (!testObst.isDestroyed()) {
                    player.setHealthPoints(player.getHealthPoints() - 25);
                    testObst.setDestroyed(true);
                }
            }

        }
    }

    private static void handleGamePause(final Timeline gameLoop, final GraphicsContext gc, final Image background) {
        isPaused = true;
        gameLoop.pause();
        if (isPaused) {
            pauseMusic();

            Timeline pauseloop = new Timeline();
            pauseloop.setCycleCount(Timeline.INDEFINITE);

            KeyFrame keyFramePause = new KeyFrame(
                    Duration.seconds(0.017),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (!isPaused) {
                                gameLoop.play();
                                playMusic();
                                pauseloop.stop();
                            }

                            gc.clearRect(0, 0, 500, 600);
                            gc.drawImage(background, 0, y);
                            gc.drawImage(background, 0, y - 600);
                            player.render(gc);

                            for (Sprite collectible : collectibles) {
                                collectible.render(gc);
                            }
                            for (Sprite obs : testObstacles) {
                                obs.render(gc);
                            }
                        }
                    });
            pauseloop.getKeyFrames().add(keyFramePause);
            pauseloop.play();

        }
    }

    private static void visualizeCollectible(GraphicsContext gc, double velocity) {
        for (Sprite collectible : collectibles) {
            collectible.setVelocity(0, velocity);
            collectible.update();
            collectible.render(gc);

            if (collectible.getBoundary().intersects(player.getBoundary())) {
                switch (collectible.getName()) {
                    case "1":      //Fuel Bottle/Pack
                        player.setPoints(player.getPoints() + 250);
                        break;
                    case "2":        //Health Pack
                        player.setPoints(player.getPoints() + 500);
                        if (player.getHealthPoints() < 100) {
                            player.setHealthPoints(player.getHealthPoints() + 25);
                        }
                        break;
                    case "3":     //Bonus
                        player.setPoints(player.getPoints() + 1000);
                        break;
                }
                collectible.setPosition(800, 800);
            }
        }
    }

    public static void clearObstaclesAndCollectibles() {
        collectibles.clear();
        testObstacles.clear();
    }

    public static void playMusic(){
        String path = "music.wav";
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
            mediaPlayer.play();
        } else {
            mediaPlayer.play();
        }
        //mediaPlayer.setAutoPlay(true);
    }

    private static void pauseMusic() {
        mediaPlayer.pause();
    }

    private static void stopMusic() {
        mediaPlayer.stop();
    }

    public static CurrentPoints getCurrentPoints() {
        return (currentPoints);
    }

    public static CurrentTime getCurrentTime() {
        return (currentTime);
    }


    public static boolean isIsPaused() {
        return isPaused;
    }

    public static void setIsPaused(boolean isPaused) {
        Game.isPaused = isPaused;
    }

    public static CurrentDistance getCurrentDstance() {
        return (currentDistance);
    }

    public static float getVelocity() {
        return velocity;
    }

    public static void setVelocity(float velocity) {
        Game.velocity = velocity;
    }
}
