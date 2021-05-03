package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.dao.ScoreDAOImpl;
import hu.alkfejl.game.GameManager;
import hu.alkfejl.model.Score;
import hu.alkfejl.shape.Shape;
import hu.alkfejl.shape.ShapeManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {

    @FXML
    public Label scoreLabel;
    @FXML
    public GridPane gamePane;
    public Pane[][] gameNodes = new Pane[10][10];
    @FXML
    public GridPane itemPane1;
    public Pane[][] itemNodes1 = new Pane[5][5];
    @FXML
    public GridPane itemPane2;
    public Pane[][] itemNodes2 = new Pane[5][5];
    @FXML
    public GridPane itemPane3;
    public Pane[][] itemNodes3 = new Pane[5][5];
    @FXML
    public Label errorLabel;
    @FXML
    public Label successLabel;
    @FXML
    public Button deleteRowBtn;
    @FXML
    public Button deleteColBtn;
    @FXML
    public Button deleteRackBtn;

    private ShapeManager shapeManager;
    private GameManager gameManager;
    private int lastCIndex=0, lastRIndex=0;
    private int hoverType = 0; //0=none, 1=row, 2=col

    private ScoreDAO dao = new ScoreDAOImpl();
//    private ScoreDAO dao = ScoreDAOImpl.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shapeManager = ShapeManager.getInstance();
        gameManager = new GameManager();
        gameManager.bindScore(scoreLabel);
        fillPane(gamePane, gameNodes);

        setOnDragDetected(itemPane1, "pane1");
        setOnDragDetected(itemPane2, "pane2");
        setOnDragDetected(itemPane3, "pane3");

        gamePane.setOnDragExited(event -> {
            gameManager.fillPane(gameNodes);
            event.consume();
        });

        gamePane.setOnDragOver(event -> {
            if(event.getGestureSource() != gamePane && event.getDragboard().hasString()){
                gameManager.fillPane(gameNodes);
                event.acceptTransferModes(TransferMode.MOVE);
                Dragboard db = event.getDragboard();
                Node node = event.getPickResult().getIntersectedNode();
                if(db.hasString()) {
                    getIndex(node);
                    Shape shape = null;
                    if(db.getString().equals("pane1"))
                        shape = gameManager.getShape1();
                    else if(db.getString().equals("pane2"))
                        shape = gameManager.getShape2();
                    else if(db.getString().equals("pane3"))
                        shape = gameManager.getShape3();
                    shapeManager.placeShape(gameNodes, shape, lastCIndex-2, lastRIndex-2,
                            gameManager.canPlace(shape, lastCIndex-2, lastRIndex-2) ? "black" : "red");
                }
            }
            event.consume();
        });

        gamePane.setOnDragDropped(event -> {
            boolean success = false;
            Dragboard db = event.getDragboard();
            Node node = event.getPickResult().getIntersectedNode();
            if(node != gamePane && db.hasString()){
                getIndex(node);
                Shape shape = null;

                if(db.getString().equals("pane1")) {
                    shape = gameManager.getShape1();
                } else if(db.getString().equals("pane2")) {
                    shape = gameManager.getShape2();
                } else if(db.getString().equals("pane3")) {
                    shape = gameManager.getShape3();
                }


                if(gameManager.canPlace(shape, lastCIndex-2, lastRIndex-2)){
                    if(db.getString().equals("pane1")) {
                        clearItem(itemPane1, itemNodes1);
                    } else if(db.getString().equals("pane2")) {
                        clearItem(itemPane2, itemNodes2);
                    } else if(db.getString().equals("pane3")) {
                        clearItem(itemPane3, itemNodes3);
                    }
                    shapeManager.placeShape(gameNodes, shape, lastCIndex-2, lastRIndex-2);
                    gameManager.addScore(1);
                    gameManager.updateBoard(gameNodes);
                    gameManager.checkGame();
                    gameManager.fillPane(gameNodes);
                }
                if(isClear()) newRack(); //Check if rack is empty
                if(!canMove()){
                    System.out.println("Game over");
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Game Over!");
                    dialog.setHeaderText("Score: " + gameManager.getScore());
                    dialog.setContentText("Please enter your name: ");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> {
                        dao.save(new Score(name, gameManager.getScore()));
                    });
                    FXMLLoader loader = App.loadFXML("/fxml/main_window.fxml");
                }
                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        gamePane.setOnMouseMoved(event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if(node != gamePane) {
                getIndex(node);
                removeClass();
                if(hoverType != 0){
                    update(hoverType == 1, hoverType == 1 ? lastRIndex : lastCIndex);
                }
            }
        });

        gamePane.setOnMouseExited(event -> removeClass());
        gamePane.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if((hoverType == 1 && gameManager.canDeleteRow()) || (hoverType == 2 && gameManager.canDeleteCol())){
                getIndex(node);
                if(hoverType == 1){
                    gameManager.removeColumn(lastRIndex);
                    gameManager.applyHelp(2);
                }
                if(hoverType == 2){
                    gameManager.removeRow(lastCIndex);
                    gameManager.applyHelp(3);
                }
                showSuccess("Deleted successfully");
                updateHelp();
                gameManager.fillPane(gameNodes);
            }
            hoverType = 0;
            removeClass();
        });
        newRack();
        updateHelp();
    }

    public void updateHelp(){
        deleteRowBtn.setText("Delete row (" + gameManager.getDeleteRow() + ")");
        deleteColBtn.setText("Delete column (" + gameManager.getDeleteCol() + ")");
        deleteRackBtn.setText("New items (" + gameManager.getDeleteRack() + ")");
    }

    public void getIndex(Node node){
        Integer cIndex = GridPane.getColumnIndex(node);
        Integer rIndex = GridPane.getRowIndex(node);
        if (cIndex == null || rIndex == null) {
            cIndex = lastCIndex;
            rIndex = lastRIndex;
        }
        lastCIndex = cIndex;
        lastRIndex = rIndex;
    }

    public void update(boolean row, int i){
        if(!row){
            for (int j = 0; j < 10; j++) {
                gameNodes[i][j].getStyleClass().add("green");
            }
        }
        else{
            for (int j = 0; j < 10; j++) {
                gameNodes[j][i].getStyleClass().add("green");
            }
        }
    }

    public void removeClass(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gameNodes[i][j].getStyleClass().remove("green");
            }
        }
    }

    public boolean canMove(){
        if(gameManager.canDeleteCol() || gameManager.canDeleteRow() || gameManager.canDeleteRack()) return true;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(!isClear(itemPane1, itemNodes1) && gameManager.canPlace(gameManager.getShape1(), i, j)) return true;
                if(!isClear(itemPane2, itemNodes2) && gameManager.canPlace(gameManager.getShape2(), i, j)) return true;
                if(!isClear(itemPane3, itemNodes3) && gameManager.canPlace(gameManager.getShape3(), i, j)) return true;
            }
        }
        return false;
    }

    @FXML
    public void onDeleteRow(){
        if(!gameManager.canDeleteRow()){
            showError(gameManager.getScore() < 50);
            return;
        }
        if(hoverType == 1){
            successLabel.setVisible(false);
            hoverType = 0;
            removeClass();
        }
        hoverType = 1;
        successLabel.setText("Click on row to delete!");
        successLabel.setVisible(true);
    }

    @FXML
    public void onDeleteCol(){
        if(!gameManager.canDeleteCol()){
            showError(gameManager.getScore() < 50);
            return;
        }
        if(hoverType == 2){
            successLabel.setVisible(false);
            hoverType = 0;
            removeClass();
        }
        hoverType = 2;
        successLabel.setText("Click on column to delete!");
        successLabel.setVisible(true);
    }

    @FXML
    public void onDeleteRack(){
        if(!gameManager.canDeleteRack()){
            showError(gameManager.getScore() < 50);
            return;
        }
        newRack();
        gameManager.applyHelp(1);
        updateHelp();
        showSuccess("Success: New Items Shown");
    }

    public void showError(boolean points){
        if(errorLabel.isVisible()) return;
        errorLabel.setText(points ? "Not enough points" : "No more uses left");
        errorLabel.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                errorLabel.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void showSuccess(String text){
        successLabel.setText(text);
        if(successLabel.isVisible()) return;
        successLabel.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                successLabel.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Integer getRowIndex(Node node){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameNodes[i][j] == node || gameNodes[i][j].getChildren().contains(node)){
                    return j;
                }
            }
        }
        return null;
    }

    public Integer getColumnIndex(Node node){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameNodes[i][j] == node || gameNodes[i][j].getChildren().contains(node)){
                    return i;
                }
            }
        }
        return null;
    }

    public void setOnDragDetected(GridPane pane, String id){
        pane.setOnDragDetected(event -> {
            Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(id);
            db.setContent(content);
            gameManager.updateBoard(gameNodes);
            event.consume();
        });
    }

    public void newRack(){
        clearRack();
        gameManager.newRack();
        shapeManager.placeShape(itemNodes1, gameManager.getShape1(), 0, 0);
        shapeManager.placeShape(itemNodes2, gameManager.getShape2(), 0, 0);
        shapeManager.placeShape(itemNodes3, gameManager.getShape3(), 0, 0);
    }

    public boolean isClear(){
        return isClear(itemPane1, itemNodes1)
            && isClear(itemPane2, itemNodes2)
            && isClear(itemPane3, itemNodes3);
    }

    public boolean isClear(GridPane pane, Pane[][] nodes){
        for (int i = 0; i < pane.getColumnCount(); i++) {
            for (int j = 0; j < pane.getRowCount(); j++) {
                if(!nodes[i][j].getChildren().isEmpty()) return false;
            }
        }
        return true;
    }

    public void clearRack(){
        clearItem(itemPane1, itemNodes1);
        clearItem(itemPane2, itemNodes2);
        clearItem(itemPane3, itemNodes3);
    }

    public void clearItem(GridPane pane, Node[][] nodes){
        pane.getChildren().clear();
        fillPane(pane, nodes);
    }

    public void fillPane(GridPane pane, Node[][] nodes){
        pane.getChildren().clear();
        for (int i = 0; i < pane.getColumnCount(); i++) {
            for (int j = 0; j < pane.getRowCount(); j++) {
                Pane p = new StackPane();
                p.getStyleClass().add("pane");
                nodes[i][j] = p;
                pane.add(p, i, j);
            }
        }
    }


    @FXML
    public void onExit(){
        Platform.exit();
    }

    @FXML
    public void showLeaderboard(){
        App.showLeaderboard();
    }

    @FXML
    public void newGame(){
        FXMLLoader fxmlLoader = App.loadFXML("/fxml/game_window.fxml");
    }
}
