package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class App extends Application{
    RectangularMap map1;
    AbstractWorldMap map2;
    ImageView[][] imagesArray;
    Stage primaryStage;
    GridPane grid = new GridPane();

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setScene(makeMenuScene());
        primaryStage.show();
    }

    private void drawFirstMap(){
//        GuiElementBox guiElementBox = new GuiElementBox();
        Vector2d lowerLeft = map1.getLowerLeft();
        Vector2d upperRight = map1.getUpperRight();
        for (int x = 0; x < map1.getWidth(); x++) {
            for (int y = 0; y < map1.getHeight(); y++) {
                grid.add(imagesArray[x][y], x - lowerLeft.x + 1, upperRight.y - y + 1);
                GridPane.setHalignment(imagesArray[x][y], HPos.CENTER);
            }
        }
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
//        for (Vector2d position: map1.getAnimals().keySet()) {
//            ArrayList<Animal> list = map1.getAnimals().get(position);
//            list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
//            ImageView image = guiElementBox.getBoxElement(list.get(0));
//            grid.add(image,position.x-lowerLeft.x+1,upperRight.y-position.y+1);
//            imagesArray[position.x][position.y] = image;
//        }
    }

    private Scene makeMapScene(){
        Vector2d lowerLeft = map1.getLowerLeft();
        Vector2d upperRight = map1.getUpperRight();
        imagesArray = map1.getImagesArray();
        Button startButton = new Button("stop");
        VBox vBox = new VBox(startButton);
        vBox.setAlignment(Pos.CENTER);
        for (int i = 0; i <= upperRight.x-lowerLeft.x+1; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i <= upperRight.y-lowerLeft.y+1; i++) {
            grid.getRowConstraints().add(new RowConstraints(20));
        }
        drawFirstMap();
        ThreadedSimulationEngine engine = new ThreadedSimulationEngine(map1);
        Thread engineThread = new Thread(engine);
        engineThread.start();
        startButton.setOnAction(event -> {
            if(startButton.getText().equals("stop")) {
                engine.setShouldRun(false);
                startButton.setText("start");
            }else {
                startButton.setText("stop");
                engine.setShouldRun(true);
            }
        });
        return new Scene(new HBox(vBox, grid), 1300, 1000);
    }

    private Scene makeMenuScene(){
        Label mapProperties = new Label("Map properties");
        Label widthLab = new Label("Map width");
        Label heightLab = new Label("Map height");
        Label jungleRatioLab = new Label("Jungle ratio");
        Label startEnergyLab = new Label("Start energy");
        Label moveEnergyLab = new Label("Energy cost of move");
        Label plantEnergyLab = new Label("Energy given by plants");
        Label startingAnimalsLab = new Label("Amount of animals starting on the map");
        TextField heightTxt = new TextField("30");
        TextField widthTxt = new TextField("30");
        TextField jungleRatioTxt = new TextField("0.1");
        TextField startEnergyTxt = new TextField("40");
        TextField moveEnergyTxt = new TextField("1");
        TextField plantEnergyTxt = new TextField("20");
        TextField startingAnimalsTxt = new TextField("15");
        HBox height = new HBox(heightLab, heightTxt);
        HBox width = new HBox(widthLab, widthTxt);
        HBox jungleRatio = new HBox(jungleRatioLab, jungleRatioTxt);
        Label mapElements = new Label("Map elements");
        HBox startEnergy = new HBox(startEnergyLab, startEnergyTxt);
        HBox plantEnergy = new HBox(plantEnergyLab, plantEnergyTxt);
        HBox moveEnergy = new HBox(moveEnergyLab, moveEnergyTxt);
        HBox startingAnimals = new HBox(startingAnimalsLab, startingAnimalsTxt);
        Button button = new Button("Start simulation");

        button.setOnAction(event -> {
            map1 = new RectangularMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()));
            primaryStage.setScene(makeMapScene());
        });

        mapProperties.setAlignment(Pos.CENTER);
        height.setAlignment(Pos.CENTER);
        width.setAlignment(Pos.CENTER);
        jungleRatio.setAlignment(Pos.CENTER);
        mapElements.setAlignment(Pos.CENTER);
        startEnergy.setAlignment(Pos.CENTER);
        plantEnergy.setAlignment(Pos.CENTER);
        moveEnergy.setAlignment(Pos.CENTER);
        button.setAlignment(Pos.CENTER_RIGHT);
        startingAnimals.setAlignment(Pos.CENTER);
        widthLab.setAlignment(Pos.CENTER_RIGHT);
        heightLab.setAlignment(Pos.CENTER_RIGHT);
        jungleRatioLab.setAlignment(Pos.CENTER_RIGHT);
        startEnergyLab.setAlignment(Pos.CENTER_RIGHT);
        startingAnimalsLab.setAlignment(Pos.CENTER_RIGHT);
        plantEnergyLab.setAlignment(Pos.CENTER_RIGHT);
        moveEnergyLab.setAlignment(Pos.CENTER_RIGHT);
        widthTxt.setAlignment(Pos.CENTER_LEFT);
        heightTxt.setAlignment(Pos.CENTER_LEFT);
        jungleRatioTxt.setAlignment(Pos.CENTER_LEFT);
        startEnergyTxt.setAlignment(Pos.CENTER_LEFT);
        startingAnimalsTxt.setAlignment(Pos.CENTER_LEFT);
        plantEnergyTxt.setAlignment(Pos.CENTER_LEFT);
        moveEnergyTxt.setAlignment(Pos.CENTER_LEFT);
        button.setPrefSize(100, 40);

        VBox menu = new VBox(mapProperties, height, width, jungleRatio, mapElements, startEnergy, moveEnergy, plantEnergy, startingAnimals, button);
        return new Scene(menu, 600, 250);
    }
}
