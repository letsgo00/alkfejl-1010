package hu.alkfejl.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shape {

    private boolean[][] shape;
    private int rotate;

    /**
     *
     * @param rotate 0 of times to rotate by 90 degrees
     * @param shape
     */
    public Shape(int rotate, String... shape){
        this.rotate = rotate;
        this.shape = new boolean[5][5];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                this.shape[i][j] = shape[i].toCharArray()[j] != ' ';
            }
        }
    }

    private Shape(boolean[][] shape){
        this.shape = shape;
        this.rotate = 0;
    }

    public int getRotation() {
        return rotate;
    }

    public boolean[][] getShape() {
        return shape;
    }

    public List<Shape> getShapes(){
        List<Shape> result = new ArrayList<>();
        result.add(new Shape(shape.clone()));
        for (int i = 0; i < rotate; i++) {
            shape = rotateMatrix(shape);
            result.add(new Shape(shape.clone()));
        }
        return result;
    }

    private boolean[][] rotateMatrix(boolean[][] matrix) {
        int size = matrix.length;
        boolean[][] ret = new boolean[size][size];

        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                ret[i][j] = matrix[size - j - 1][i]; //***

        return ret;
    }
}
