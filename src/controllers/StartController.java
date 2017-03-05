package controllers;

import dataHandler.Constants;
import dataHandler.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mapHandlers.Levels.FirstLevel;
import mapHandlers.Track;
import stageHandler.StageManager;
import stageHandler.StageManagerImpl;

import java.io.IOException;

public class StartController {
    @FXML
    private Button startBtn;

    @FXML

    private void startNewGame() throws IOException {
        Stage currentStage = (Stage)this.startBtn.getScene().getWindow();
        StageManager manager = new StageManagerImpl();
        FXMLLoader loader = manager.loadSceneToStage(currentStage,Constants.CHOOSE_CAR_VIEW_PATH,null);
//        Track track = new FirstLevel();
//        PlayerData.getInstance().returnPlayer(track.getRunTrack().getPlayer().getName());
//        Stage currentStage = (Stage)this.startBtn.getScene().getWindow();
//        StageManager manager = new StageManagerImpl();
//
//        FXMLLoader loader = manager.loadSceneToStage(currentStage, Constants.GAME_PLAY_VIEW_PATH,null);
//        AnchorPane root = manager.getRoot();
//        track.createBackground(root);
    }

    @FXML
    private void showHighScores() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(Constants.HIGH_SCORE_DIALOG_TITLE);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(Constants.HIGH_SCORES_DIALOG));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(Constants.DIALOG_MESSAGE);
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    @FXML
    private void onClose() {
        Platform.exit();
    }
}
