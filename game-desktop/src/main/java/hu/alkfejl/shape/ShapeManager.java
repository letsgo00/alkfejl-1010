package hu.alkfejl.shape;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShapeManager {

    private static ShapeManager instance = new ShapeManager();

    public static ShapeManager getInstance() {
        return instance;
    }
    private List<Shape> shapes = new ArrayList<>();

    private ShapeManager(){
        shapes.addAll(new Shape(0,"     ","     ","  X  ","     ","     ").getShapes()); // 1
        shapes.addAll(new Shape(1,"     ","     ","  XX ","     ","     ").getShapes()); // 2
        shapes.addAll(new Shape(1,"     ","     "," XXX ","     ","     ").getShapes()); // 2
        shapes.addAll(new Shape(1,"     ","     "," XXXX","     ","     ").getShapes()); // 2
        shapes.addAll(new Shape(1,"     ","     ","XXXXX","     ","     ").getShapes()); // 2
        shapes.addAll(new Shape(3,"     ","  X  ","  XX ","     ","     ").getShapes()); // 4
        shapes.addAll(new Shape(0,"     ","  XX ","  XX ","     ","     ").getShapes()); // 1
        shapes.addAll(new Shape(3,"     "," X   "," X   "," XXX ","     ").getShapes()); // 4
        shapes.addAll(new Shape(0,"     "," XXX "," XXX "," XXX ","     ").getShapes()); // 1
        System.out.println("shapes.size() = " + shapes.size());
    }

    public Shape getNewShape(){
        Random random = new Random();
        return shapes.get(random.nextInt(shapes.size()));
    }
    public Shape placeShape(Pane[][] panes, Shape shape, int startX, int startY){
        return placeShape(panes, shape, startX, startY, null);
    }

    public Shape placeShape(Pane[][] panes, Shape shape, int startX, int startY, String c){
        if (shape == null) return null;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(!shape.getShape()[i][j]) continue;
                if(startX+i >= 10 || startY+j >= 10 || startX+i < 0 || startY+j < 0) continue;
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(20);
                rectangle.setWidth(20);
                if(c != null) rectangle.getStyleClass().add(c);
                panes[startX+i][startY+j].getChildren().add(rectangle);
            }
        }
        return shape;
    }
}
