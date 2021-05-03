package hu.alkfejl.controller;

import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.dao.ScoreDAOImpl;
import hu.alkfejl.model.Score;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderboardWindowController implements Initializable {

    private ScoreDAO scoreDao = new ScoreDAOImpl();
//    private ScoreDAO scoreDao = ScoreDAOImpl.getInstance();

    @FXML
    private TableView<Score> scoreTable;
    @FXML
    private TableColumn<Score, String> positionColumn;
    @FXML
    private TableColumn<Score, String> nameColumn;
    @FXML
    private TableColumn<Score, String> scoreColumn;
    @FXML
    private TableColumn<Score, String> dateColumn;

    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreTable.getItems().setAll(scoreDao.getTop10());
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onClose(){
        stage.close();
    }
}
