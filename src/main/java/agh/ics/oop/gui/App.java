package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class App extends Application{
    private RectangularMap mapRec;
    private FoldedMap mapFol;
    private ImageView[][] imagesArrayRec;
    private ImageView[][] imagesArrayFol;
    private Image transparent;
    private Stage primaryStage;
    private final GridPane gridRec = new GridPane();
    private final GridPane gridFol = new GridPane();
    private ThreadedSimulationEngine engineRec;
    private ThreadedSimulationEngine engineFol;
    {
        try {
            transparent = new Image(new FileInputStream(".\\src\\main\\resources\\transparent.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setScene(makeMenuScene());
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
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
        engineRec = new ThreadedSimulationEngine(mapRec);
        engineFol = new ThreadedSimulationEngine(mapFol);
        Statistics statRec = new Statistics(mapRec, engineRec);
        Statistics statFol = new Statistics(mapFol, engineFol);
        Button startButtonRec = new Button("stop");
        Button startButtonFol = new Button("stop");
        Button highlightRec = new Button("highlight animals with dominant genotype");
        Button highlightFol = new Button("highlight animals with dominant genotype");
        Button saveStatsRec = new Button("Save statistics to file");
        Button saveStatsFol = new Button("Save statistics to file");
        startButtonRec.setAlignment(Pos.CENTER);
        startButtonFol.setAlignment(Pos.CENTER);
        setUpImageArray(mapRec, imagesArrayRec, startButtonRec, statRec);
        setUpImageArray(mapFol, imagesArrayFol, startButtonFol, statFol);
        for (int i = 0; i <= upperRight.x-lowerLeft.x+1; i++) {
            gridRec.getColumnConstraints().add(new ColumnConstraints(20));
            gridFol.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i <= upperRight.y-lowerLeft.y+1; i++) {
            gridRec.getRowConstraints().add(new RowConstraints(20));
            gridFol.getRowConstraints().add(new RowConstraints(20));
        }
        drawFirstMap();
        startButtonRec.setOnMouseClicked(event -> {
            if(startButtonRec.getText().equals("stop")) {
                engineRec.setShouldRun(false);
                startButtonRec.setText("start");
            }else {
                startButtonRec.setText("stop");
                engineRec.setShouldRun(true);
            }
        });
        startButtonFol.setOnMouseClicked(event -> {
            if(startButtonFol.getText().equals("stop")) {
                engineFol.setShouldRun(false);
                startButtonFol.setText("start");
            }else {
                startButtonFol.setText("stop");
                engineFol.setShouldRun(true);
            }
        });
        highlightRec.setOnMouseClicked(event -> {
            if (startButtonRec.getText().equals("start")) {
                mapRec.highlight();
            }
        });
        highlightFol.setOnMouseClicked(event -> {
            if (startButtonFol.getText().equals("start")) {
                mapFol.highlight();
            }
        });
        saveStatsRec.setOnMouseClicked(event -> {
            if (startButtonRec.getText().equals("start")) {
                statRec.savetoFile();
            }
        });
        saveStatsFol.setOnMouseClicked(event -> {
            if (startButtonFol.getText().equals("start")) {
                statFol.savetoFile();
            }
        });
        VBox plotsRec = new VBox(new Label("Animal amount"), statRec.plotAnimals(), new Label("Grass amount"),
                statRec.plotGrass(), new Label("Average energy"), statRec.plotEnergy(), new Label("Average Lifetime od dead animals"),
                statRec.plotLifeTime(), new Label("Average amount of children for living animals"),statRec.plotChildren());
        VBox plotsFol = new VBox(new Label("Animal amount"), statFol.plotAnimals(), new Label("Grass amount"),
                statFol.plotGrass(), new Label("Average energy"), statFol.plotEnergy(), new Label("Average Lifetime od dead animals"),
                statFol.plotLifeTime(), new Label("Average amount of children for living animals"), statFol.plotChildren());
        Thread engineThreadRec = new Thread(engineRec);
        Thread engineThreadFol = new Thread(engineFol);
        engineThreadRec.start();
        engineThreadFol.start();
        return new Scene(new HBox(plotsRec,new VBox(new Label("Dominant genotype"), new HBox(startButtonRec, statRec.getDominantGenotypeLabel()),
                gridRec, highlightRec, saveStatsRec, statRec.trackedStats()),
                new VBox(new Label("Dominant genotype"), new HBox(startButtonFol, statFol.getDominantGenotypeLabel()),
                        gridFol, highlightFol, saveStatsFol, statFol.trackedStats()), plotsFol), 1920, 1080);
    }

    private Scene makeMenuScene(){
//        Label mapProperties = new Label("Map properties");
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
        TextField startEnergyTxt = new TextField("60");
        TextField moveEnergyTxt = new TextField("1");
        TextField plantEnergyTxt = new TextField("30");
        TextField startingAnimalsTxt = new TextField("70");
        HBox height = new HBox(heightLab, heightTxt);
        HBox width = new HBox(widthLab, widthTxt);
        HBox jungleRatio = new HBox(jungleRatioLab, jungleRatioTxt);
//        Label mapElements = new Label("Map elements");
        HBox startEnergy = new HBox(startEnergyLab, startEnergyTxt);
        HBox plantEnergy = new HBox(plantEnergyLab, plantEnergyTxt);
        HBox moveEnergy = new HBox(moveEnergyLab, moveEnergyTxt);
        HBox startingAnimals = new HBox(startingAnimalsLab, startingAnimalsTxt);
        Button button = new Button("Start simulation");
        CheckBox magicRec = new CheckBox();
        CheckBox magicFol = new CheckBox();
        magicRec.setText("Should rectangular map be magic?");
        magicFol.setText("Should folded map be magic?");
        HBox magic = new HBox(magicRec, magicFol);

        button.setOnAction(event -> {
            imagesArrayRec = new ImageView[Integer.parseInt(widthTxt.getText())][Integer.parseInt(heightTxt.getText())];
            imagesArrayFol = new ImageView[Integer.parseInt(widthTxt.getText())][Integer.parseInt(heightTxt.getText())];
            mapRec = new RectangularMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()), magicRec.isSelected(), imagesArrayRec);
            mapFol = new FoldedMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()), magicFol.isSelected(), imagesArrayFol);
            primaryStage.setScene(makeMapScene());
            primaryStage.setMaximized(true);
        });

        magic.setAlignment(Pos.CENTER);
        height.setAlignment(Pos.CENTER);
        width.setAlignment(Pos.CENTER);
        jungleRatio.setAlignment(Pos.CENTER);
        startEnergy.setAlignment(Pos.CENTER);
        plantEnergy.setAlignment(Pos.CENTER);
        moveEnergy.setAlignment(Pos.CENTER);
        button.setAlignment(Pos.CENTER);
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

        VBox menu = new VBox(height, width, jungleRatio,magic,
                startEnergy, moveEnergy, plantEnergy, startingAnimals, button);
        menu.setAlignment(Pos.CENTER);
        return new Scene(menu, 400, 250);
    }

    private void setUpImageArray(AbstractWorldMap map, ImageView[][] imagesArray, Button startButton, Statistics stats){
        for (int x = 0; x < mapRec.getWidth(); x++) {
            for (int y = 0; y < mapRec.getHeight(); y++) {
                ImageView image = new ImageView(transparent);
                image.setPickOnBounds(true);
                image.setOnMouseClicked(event -> {
                    if (image.getUserData() != null && image.getUserData().getClass().equals(Animal.class) &&
                            startButton.getText().equals("start")){
                        map.setTrackedAnimal((Animal) image.getUserData());
                        map.setTrackedAnimalChildren(0);
                        map.clearOffspring();
                        map.getTrackedAnimal().setOffspring(true);
                        stats.updateTrackedStats();
                    }
                });
                image.setFitWidth(18);
                image.setFitHeight(18);
                imagesArray[x][y] = image;
            }
        }
    }
}
