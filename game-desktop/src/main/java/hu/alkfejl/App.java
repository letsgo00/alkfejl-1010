package hu.alkfejl;

import hu.alkfejl.controller.LeaderboardWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setResizable(false);
        loadFXML("/fxml/main_window.fxml");
        stage.show();
    }


    public static FXMLLoader loadFXML(String fxml) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = null;
        try {
            Parent root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add("/css/style.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        return loader;
    }

    public static void showLeaderboard(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/leaderboard_window.fxml"));
        try {
            Parent root = loader.load();
            LeaderboardWindowController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}