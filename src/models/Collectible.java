package models;

import GameEngine.Notification;
import dataHandler.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Collectible extends Sprite {

    private int bonusCoefficient;
    private Player player;
    private List<Collectible> collectibles;
    private boolean isImmortal;
    private boolean isDoublePtsOn;
    private double immortalityTimer;
    private double doublePtsTimer;

    public Collectible() {}

    public Collectible(Player player) {
        this.bonusCoefficient = 1;
        this.player = player;
        this.collectibles = new ArrayList<>();
        isImmortal = false;
        isDoublePtsOn = false;
    }

    private String getCollectibleType(){
        switch (this.getName()){
            case "collectable1_half_size":
                return "fuelBottle";
            case "collectable2_half_size":
                return "health";
            case "collectable3_half_size":
                return "doublePts";
            case "collectable4_half_size":
                return "immortality";
            case "collectable5_half_size":
                return "armageddonsPower";
        }
        return "bonusPts";
    }

    private double getImmortalityTimer() {
        return immortalityTimer;
    }

    private void setImmortalityTimer(double immortalityTimer) {
        this.immortalityTimer = immortalityTimer;
    }

    private double getDoublePtsTimer() {
        return doublePtsTimer;
    }

    private void setDoublePtsTimer(double doublePtsTimer) {
        this.doublePtsTimer = doublePtsTimer;
    }

    public void addCollectible(Collectible collectible) {
        collectibles.add(collectible);
    }

    public static Collectible generateCollectible() {

        String[] collectibles= Constants.COLLECTABLE_LIST_SMALL;
        String random=collectibles[new Random().nextInt(collectibles.length)];

        Random collectibleX = new Random();
        String stringDirectory = Constants.IMAGES_PATH + random + ".png";

        Collectible collectible = new Collectible();
        collectible.setName(random);
        collectible.setImage(stringDirectory);
        collectible.setPosition(50 + collectibleX.nextInt(300), -60);

        return collectible;
    }

    public String visualizeCollectible(GraphicsContext gc, double velocity, Stage currentStage) {
        for (Collectible collectible : collectibles) {
            collectible.setVelocity(0, velocity);
            collectible.update();
            collectible.render(gc);

            if (collectible.getBoundary().intersects(player.getBoundary())) {
                switch (collectible.getCollectibleType()) {
                    case Constants.FUEL_BOTTLE_STRING:
                        player.addPoints(Constants.FUEL_TANK_BONUS*bonusCoefficient);

                        Notification.showPopupMessage("fuel", currentStage);
                        collectible.setPosition(Constants.DESTROY_OBJECT_COORDINATES, Constants.DESTROY_OBJECT_COORDINATES);
                        return Constants.FUEL_BOTTLE_STRING;
                    case Constants.HEALTH_STRING:
                        player.addPoints(Constants.HEALTH_PACK_BONUS_POINTS*bonusCoefficient);
                        if (player.getHealthPoints() < Constants.HEALTH_BAR_MAX) {
                            player.setHealthPoints(Math.min(player.getHealthPoints() + Constants.HEALTH_BONUS, Constants.HEALTH_BAR_MAX));
                        }
                        collectible.setPosition(Constants.DESTROY_OBJECT_COORDINATES, Constants.DESTROY_OBJECT_COORDINATES);
                        return Constants.HEALTH_STRING;
                    case Constants.DOUBLE_POINTS_STRING:
                        player.setPoints(player.getPoints() + Constants.DOUBLE_BONUS_POINTS*bonusCoefficient);
                        if (!isDoublePtsOn) {
                            startDoublePtsTimer();
                        }
                        collectible.setPosition(Constants.DESTROY_OBJECT_COORDINATES, Constants.DESTROY_OBJECT_COORDINATES);
                        return Constants.DOUBLE_POINTS_STRING;
                    case Constants.IMMORTALITY_STRING:
                        player.addPoints(Constants.IMMORTALITY_BONUS*bonusCoefficient);
                        if (!isImmortal) {
                            player.addPoints( Constants.ARMAGEDDONS_BONUS*bonusCoefficient);
                            startImmortalityTimer();
                        }
                        collectible.setPosition(Constants.DESTROY_OBJECT_COORDINATES, Constants.DESTROY_OBJECT_COORDINATES);
                        return Constants.DOUBLE_POINTS_STRING;
                    case Constants.ARMAGEDDON_STRING:
                        player.addPoints( Constants.ARMAGEDDONS_BONUS*bonusCoefficient);
                        collectible.setPosition(Constants.DESTROY_OBJECT_COORDINATES, Constants.DESTROY_OBJECT_COORDINATES);
                        return Constants.ARMAGEDDON_STRING;
                }

            }
        }
        return null;
    }

    private void startDoublePtsTimer() {
        isDoublePtsOn = true;
        bonusCoefficient = 2;
        this.setDoublePtsTimer(Constants.DOUBLE_PTS_DURATION / Constants.FRAMES_PER_SECOND);
    }

    public void updateDoublePtsStatus() {
        this.setDoublePtsTimer(this.getDoublePtsTimer() - 1);
        if (this.getDoublePtsTimer() < 0) {
            isDoublePtsOn = false;
            bonusCoefficient = 1;
            System.out.println("double points off");
        }
    }

    private void startImmortalityTimer() {
        isImmortal = true;
        this.setImmortalityTimer(Constants.IMMORTALITY_DURATION / Constants.FRAMES_PER_SECOND);
    }

    public void updateImmortalityStatus() {
        this.setImmortalityTimer(this.getImmortalityTimer() - 1);
        if (this.getImmortalityTimer() < 0) {
            isImmortal = false;
            System.out.println("immortality off");
        }
    }

    public final int getBonusCoefficient() {
        return bonusCoefficient;
    }

    public void updateStatus() {
        if (isImmortal) {
            this.updateImmortalityStatus();
        }
        if (isDoublePtsOn) {
            this.updateDoublePtsStatus();
        }
    }

    public boolean isImmortal() {
        return isImmortal;
    }
}
