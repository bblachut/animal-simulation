package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application{
    RectangularMap mapRec;
    FoldedMap mapFol;
    ImageView[][] imagesArrayRec;
    ImageView[][] imagesArrayFol;
    Stage primaryStage;
    GridPane gridRec = new GridPane();
    GridPane gridFol = new GridPane();

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setScene(makeMenuScene());
        primaryStage.show();
    }

    private void drawFirstMap(){
        Vector2d lowerLeft = mapRec.getLowerLeft();
        Vector2d upperRight = mapRec.getUpperRight();
        for (int x = 0; x < mapRec.getWidth(); x++) {
            for (int y = 0; y < mapRec.getHeight(); y++) {
                gridRec.add(imagesArrayRec[x][y], x - lowerLeft.x + 1, upperRight.y - y + 1);
                gridFol.add(imagesArrayFol[x][y], x - lowerLeft.x + 1, upperRight.y - y + 1);
                GridPane.setHalignment(imagesArrayRec[x][y], HPos.CENTER);
                GridPane.setHalignment(imagesArrayFol[x][y], HPos.CENTER);
            }
        }
        Label cornerLabelRec = new Label("y/x");
        Label cornerLabelFol = new Label("y/x");
        gridRec.add(cornerLabelRec, 0, 0);
        gridFol.add(cornerLabelFol, 0, 0);
        GridPane.setHalignment(cornerLabelRec, HPos.CENTER);
        GridPane.setHalignment(cornerLabelFol, HPos.CENTER);
        for (int i = lowerLeft.x; i <= upperRight.x; i++) {
            Label labelRec = new Label(String.valueOf(i));
            Label labelFol = new Label(String.valueOf(i));
            gridRec.add(labelRec,i-lowerLeft.x+1, 0);
            gridFol.add(labelFol,i-lowerLeft.x+1, 0);
            GridPane.setHalignment(labelRec, HPos.CENTER);
            GridPane.setHalignment(labelFol, HPos.CENTER);
        }
        for (int i = lowerLeft.y; i <= upperRight.y; i++) {
            Label labelRec = new Label(String.valueOf(i));
            Label labelFol = new Label(String.valueOf(i));
            gridRec.add(labelRec,0, upperRight.y-i+1);
            gridFol.add(labelFol,0, upperRight.y-i+1);
            GridPane.setHalignment(labelRec, HPos.CENTER);
            GridPane.setHalignment(labelFol, HPos.CENTER);
        }
    }

    private Scene makeMapScene(){
        Vector2d lowerLeft = mapRec.getLowerLeft();
        Vector2d upperRight = mapRec.getUpperRight();
        imagesArrayRec = mapRec.getImagesArray();
        imagesArrayFol = mapFol.getImagesArray();
        Button startButtonRec = new Button("stop");
        Button startButtonFol = new Button("stop");
        startButtonRec.setAlignment(Pos.CENTER);
        startButtonFol.setAlignment(Pos.CENTER);
        for (int i = 0; i <= upperRight.x-lowerLeft.x+1; i++) {
            gridRec.getColumnConstraints().add(new ColumnConstraints(20));
            gridFol.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i <= upperRight.y-lowerLeft.y+1; i++) {
            gridRec.getRowConstraints().add(new RowConstraints(20));
            gridFol.getRowConstraints().add(new RowConstraints(20));
        }
        drawFirstMap();
        ThreadedSimulationEngine engineRec = new ThreadedSimulationEngine(mapRec);
        ThreadedSimulationEngine engineFol = new ThreadedSimulationEngine(mapFol);
        Thread engineThreadRec = new Thread(engineRec);
        Thread engineThreadFol = new Thread(engineFol);
        engineThreadRec.start();
        engineThreadFol.start();
        startButtonRec.setOnAction(event -> {
            if(startButtonRec.getText().equals("stop")) {
                engineRec.setShouldRun(false);
                startButtonRec.setText("start");
            }else {
                startButtonRec.setText("stop");
                engineRec.setShouldRun(true);
            }
        });
        startButtonFol.setOnAction(event -> {
            if(startButtonFol.getText().equals("stop")) {
                engineFol.setShouldRun(false);
                startButtonFol.setText("start");
            }else {
                startButtonFol.setText("stop");
                engineFol.setShouldRun(true);
            }
        });
        return new Scene(new HBox(startButtonRec, gridRec, startButtonFol, gridFol), 1300, 1000);
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
            mapRec = new RectangularMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()));
            mapFol = new FoldedMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
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
