package hu.alkfejl.controller;

import hu.alkfejl.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class MainWindowController {


    @FXML
    public void newGame(){
        FXMLLoader fxmlLoader = App.loadFXML("/fxml/game_window.fxml");
    }

    @FXML
    public void onExit(){
        Platform.exit();
    }

    @FXML
    public void showLeaderboard(){
        App.showLeaderboard();
    }
}
