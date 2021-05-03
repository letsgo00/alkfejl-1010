package hu.alkfejl.model;


import javafx.beans.property.*;

import java.time.LocalDate;

public class Score {
    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private IntegerProperty score = new SimpleIntegerProperty(this, "score");
    private IntegerProperty position = new SimpleIntegerProperty(this, "position");
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(this, "date");

    public Score(){

    }

    public Score(String name, int score){
        if(name == null || name.trim().equals("")) return;
        this.setName(name.trim());
        this.setScore(score);
        this.setDate(LocalDate.now());
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }
}
