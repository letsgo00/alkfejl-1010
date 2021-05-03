package hu.alkfejl.game;

import hu.alkfejl.shape.Shape;
import hu.alkfejl.shape.ShapeManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class GameManager {

    private IntegerProperty score = new SimpleIntegerProperty(this, "score");
    private boolean[][] board;
    private Shape shape1;
    private Shape shape2;
    private Shape shape3;

    private int deleteRack;
    private int deleteRow;
    private int deleteCol;


    public GameManager(){
        newGame();
    }

    public void bindScore(Label label){
        label.textProperty().bind(score.asString());
    }

    public void newGame(){
        board = new boolean[10][10];
        newRack();
        deleteRack = 3;
        deleteRow = 3;
        deleteCol = 3;
        score.set(0);
    }

    public void newRack(){
        shape1 = ShapeManager.getInstance().getNewShape();
        shape2 = ShapeManager.getInstance().getNewShape();
        shape3 = ShapeManager.getInstance().getNewShape();
    }

    public boolean canDeleteRack(){
        return deleteRack > 0 && score.get() >= 50;
    }

    public boolean canDeleteRow(){
        return deleteRow > 0 && score.get() >= 50;
    }

    public boolean canDeleteCol(){
        return deleteCol > 0 && score.get() >= 50;
    }

    public int getScore() {
        return score.get();
    }

    public void fillPane(Pane[][] nodes){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                nodes[i][j].getChildren().clear();
                if(board[i][j]){
                    Rectangle rectangle = new Rectangle();
                    rectangle.setHeight(40);
                    rectangle.setWidth(40);

                    nodes[i][j].getChildren().add(rectangle);
                }
            }
        }
    }

    public void updateBoard(Pane[][] nodes){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = !nodes[i][j].getChildren().isEmpty();
            }
        }
    }

    public boolean canPlace(Shape shape, int startX, int startY){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(!shape.getShape()[i][j]) continue;
                if(startX+i >= 10 || startY+j >= 10 || startX+i < 0 || startY+j < 0) return false;
                if(board[startX+i][startY+j]) return false;
            }
        }
        return true;
    }

    public void checkGame(){
        boolean fullRow;
        boolean fullCol;
        for (int i = 0; i < 10; i++) {
            fullRow = true;
            fullCol = true;
            for (int j = 0; j < 10; j++) {
                if (!board[i][j]) fullRow = false;
                if (!board[j][i]) fullCol = false;
            }
            if(fullRow){
                removeRow(i);
                addScore(10);
            }
            if (fullCol){
                removeColumn(i);
                addScore(10);
            }
        }
    }

    public void addScore(int i){
        score.set(score.get()+i);
    }

    public void removeRow(int row){
        System.out.println("row = " + row);
        for (int i = 0; i < 10; i++) {
            board[row][i] = false;
        }
    }

    public void removeColumn(int column){
        System.out.println("column = " + column);
        for (int i = 0; i < 10; i++) {
            board[i][column] = false;
        }
    }

    public Shape getShape1() {
        return shape1;
    }

    public Shape getShape2() {
        return shape2;
    }

    public Shape getShape3() {
        return shape3;
    }

    /**
     *
     * @param type 1=rack 2=row 3=col
     */
    public void applyHelp(int type) {
        if(type == 1) deleteRack--;
        else if(type == 2) deleteRow--;
        else if(type == 3) deleteCol--;
        else return;
        score.set(score.get()-50);
    }

    public int getDeleteCol() {
        return deleteCol;
    }

    public int getDeleteRack() {
        return deleteRack;
    }

    public int getDeleteRow() {
        return deleteRow;
    }
}
