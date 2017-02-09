package Controllers;

import DataHandler.Player;
import DataHandler.PlayerData;
import MapHandlers.Track;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML
    public AnchorPane loginPage;
    public static AnchorPane homePage;
    @FXML
    private Button loginBtn;
    @FXML
    private TextField playerName;
    @FXML
    private Button showScoresBtn;
    @FXML
    private Button closeBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Button chooseCarBtn;
    @FXML
    public void showHighScores() {
        showHighScoresDialog();
    }

    public static Player player;

    public static void MainController(){

    }

    @FXML
    public void chooseCar() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/chooseCar.fxml"));
        AnchorPane windowPane = (AnchorPane) root.lookup("#chooseCarPage");
        Main.windowPane=windowPane;
        Stage stage = (Stage) chooseCarBtn.getScene().getWindow();
        stage.setTitle("Choose Your Car");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();


    }

    private void showHighScoresDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Best Slav Ranking");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/highScoresDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Can't load the High Scores dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    @FXML
    public void startNewGame() {

        Track.initializeLevel(1);

        showScoresBtn.setVisible(false);
        startBtn.setVisible(false);
        closeBtn.setVisible(false);
    }

    @FXML
    public void onClose() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void showStartPage() throws IOException {
        String name = playerName.getText().trim();
        if ("".equals(name)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have to fill in the username field!");
            alert.setContentText("Please click on OK to retry!");
            alert.setHeaderText("You have to fill in the username field!");
            Optional<ButtonType> result = alert.showAndWait();
        } else if (!PlayerData.getInstance().checkForPlayer(name)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login with this username");
            alert.setHeaderText("Login as user: " + name);
            alert.setContentText("Are you sure? Press OK to continue, or Cancel to abort.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                player = PlayerData.getInstance().returnPlayer(name);
                renderStartMenu();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Create new user");
            alert.setHeaderText("Create new user: " + name);
            alert.setContentText("Are you sure? Press OK to continue, or Cancel to abort.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                player = new Player(name, 0L, 0.0, 0L, 0L, 100);
                /* remove the comments to save every newly created player*/
//                PlayerData.getInstance().addPlayer(player);
//                PlayerData.getInstance().storePlayersData();
                renderStartMenu();
            }
        }
    }

    private void renderStartMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/startPage.fxml"));
        AnchorPane windowPane = (AnchorPane) root.lookup("#homePage");
        Main.windowPane=windowPane;
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setTitle("Race Game");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

}
