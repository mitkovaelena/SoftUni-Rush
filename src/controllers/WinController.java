package controllers;

import dataHandler.PlayerData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.constants.ViewsConstants;
import utils.stages.StageManager;
import utils.stages.StageManagerImpl;

import java.io.IOException;

public class WinController {

    @FXML
    private Button quitBtn;

    public void restartGame(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) this.quitBtn.getScene().getWindow();
        StageManager manager = new StageManagerImpl();
        manager.loadSceneToStage(currentStage, ViewsConstants.START_FXML_PATH);
        PlayerData.getInstance().updatePlayer(PlayerData.getInstance().getCurrentPlayer());
    }

    @FXML
    private void quitGame(ActionEvent actionEvent) {
        Platform.exit();
    }
}
