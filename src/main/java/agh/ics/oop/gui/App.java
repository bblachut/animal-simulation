package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class App extends Application {
    GrassField map = new GrassField(10);

    @Override
    public void start(Stage primaryStage){
        HashMap<Vector2d, Animal> animals = map.getAnimals();
        HashMap<Vector2d, Grass> grassOnMap = map.getGrass();
        GuiElementBox guiElementBox = new GuiElementBox();
        Vector2d lowerLeft = map.getLowerLeft();
        Vector2d upperRight = map.getUpperRight();
        Label cornerLabel = new Label("y/x");
        GridPane grid = new GridPane();
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getColumnConstraints().add(new ColumnConstraints(30));
        Scene scene = new Scene(grid, 400, 400);
        grid.add(cornerLabel, 0, 0);
        GridPane.setHalignment(cornerLabel, HPos.CENTER);
        for (int i = lowerLeft.x; i <= upperRight.x; i++) {
            Label label = new Label(String.valueOf(i));
            grid.add(label,i-lowerLeft.x+1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            grid.getColumnConstraints().add(new ColumnConstraints(30));
        }
        for (int i = lowerLeft.y; i <= upperRight.y; i++) {
            Label label = new Label(String.valueOf(i));
            grid.add(label,0, upperRight.y-i+1);
            grid.getRowConstraints().add(new RowConstraints(30));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (Vector2d position: animals.keySet()) {
            VBox verticalBox = guiElementBox.getBoxElement(animals.get(position));
            grid.add(verticalBox,position.x-lowerLeft.x+1,upperRight.y-position.y+1);
            GridPane.setHalignment(verticalBox, HPos.CENTER);
        }
        for (Vector2d position: grassOnMap.keySet()) {
            VBox verticalBox = guiElementBox.getBoxElement(grassOnMap.get(position));
            grid.add(verticalBox,position.x-lowerLeft.x+1,upperRight.y-position.y+1);
            GridPane.setHalignment(verticalBox, HPos.CENTER);
        }
        grid.setGridLinesVisible(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init(){
        try {
            String[] args = this.getParameters().getRaw().toArray(new String[0]);
            MoveDirection[] directions = new OptionsParser().parse(args);
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(10,10) };
            IEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
            System.out.println(map);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
