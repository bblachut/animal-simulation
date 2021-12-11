package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application implements IEngineObserver{
    GrassField map = new GrassField(10);
    GridPane grid = new GridPane();

    @Override
    public void start(Stage primaryStage){
        Vector2d lowerLeft = map.getLowerLeft();
        Vector2d upperRight = map.getUpperRight();
        primaryStage.show();
        primaryStage.setScene(makeScene());
        for (int i = 0; i <= upperRight.x-lowerLeft.x+1; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(35));
        }
        for (int i = 0; i <= upperRight.y-lowerLeft.y+1; i++) {
            grid.getRowConstraints().add(new RowConstraints(35));
        }
        drawMap();
    }

    private void drawMap(){
        GuiElementBox guiElementBox = new GuiElementBox();
        grid.setGridLinesVisible(true);
        Vector2d lowerLeft = map.getLowerLeft();
        Vector2d upperRight = map.getUpperRight();
        Label cornerLabel = new Label("y/x");
        grid.add(cornerLabel, 0, 0);
        GridPane.setHalignment(cornerLabel, HPos.CENTER);
        for (int i = lowerLeft.x; i <= upperRight.x; i++) {
            Label label = new Label(String.valueOf(i));
            grid.add(label,i-lowerLeft.x+1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = lowerLeft.y; i <= upperRight.y; i++) {
            Label label = new Label(String.valueOf(i));
            grid.add(label,0, upperRight.y-i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (Vector2d position: map.getAnimals().keySet()) {
            VBox verticalBox = guiElementBox.getBoxElement(map.getAnimals().get(position));
            grid.add(verticalBox,position.x-lowerLeft.x+1,upperRight.y-position.y+1);
            GridPane.setHalignment(verticalBox, HPos.CENTER);
        }
        for (Vector2d position: map.getGrass().keySet()) {
            if(!map.getAnimals().containsKey(position)) {
                VBox verticalBox = guiElementBox.getBoxElement(map.getGrass().get(position));
                grid.add(verticalBox, position.x - lowerLeft.x + 1, upperRight.y - position.y + 1);
                GridPane.setHalignment(verticalBox, HPos.CENTER);
            }
        }
    }

    public Scene makeScene(){
        Button startButton = new Button("start");
        TextField directionsField = new TextField();
        VBox vBox = new VBox(directionsField, startButton);
        vBox.setAlignment(Pos.CENTER);
        startButton.setOnAction(event -> {
            map = new GrassField(10);
            MoveDirection[] directions = new OptionsParser().parse(directionsField.getText().split(" "));
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(10,10) };
            ThreadedSimulationEngine engine = new ThreadedSimulationEngine(directions ,map, positions);
            engine.addObserver(this);
            drawMap();
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });
        return new Scene(new HBox(vBox, grid), 600, 450);
    }

    @Override
    public void moved() {
        Platform.runLater(() -> {
            Node node = grid.getChildren().get(0);
            grid.getChildren().clear();
            grid.getChildren().add(0,node);
            drawMap();
        });
    }
}
