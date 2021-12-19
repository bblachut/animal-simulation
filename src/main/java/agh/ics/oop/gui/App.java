package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application{
    private RectangularMap mapRec;
    private FoldedMap mapFol;
    private ImageView[][] imagesArrayRec;
    private ImageView[][] imagesArrayFol;
    private Stage primaryStage;
    private final GridPane gridRec = new GridPane();
    private final GridPane gridFol = new GridPane();
    private ThreadedSimulationEngine engineRec;
    private ThreadedSimulationEngine engineFol;

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
        engineRec = new ThreadedSimulationEngine(mapRec);
        engineFol = new ThreadedSimulationEngine(mapFol);
        Statistics statRec = new Statistics(mapRec, engineRec);
        Statistics statFol = new Statistics(mapFol, engineFol);
        Button startButtonRec = new Button("stop");
        Button startButtonFol = new Button("stop");
        Button highlightRec = new Button("highlight animals with dominant genotype");
        Button highlightFol = new Button("highlight animals with dominant genotype");
        startButtonRec.setAlignment(Pos.CENTER);
        startButtonFol.setAlignment(Pos.CENTER);
        imagesArrayRec = mapRec.getImagesArray();
        imagesArrayFol = mapFol.getImagesArray();
//        for (int x = 0; x < mapRec.getWidth(); x++) {
//            for (int y = 0; y < mapRec.getHeight(); y++) {
//                ImageView imageFol = imagesArrayFol[x][y];
//                ImageView imageRec = imagesArrayFol[x][y];
//                imageRec.setPickOnBounds(true);
//                imageFol.setPickOnBounds(true);
//                imageRec.setOnMouseClicked(event -> {
//                    System.out.println("chuj");
//                    if (imageRec.getUserData() != null && imageRec.getUserData().getClass().equals(Animal.class) && startButtonRec.getText().equals("start")){
//                        mapRec.setTrackedAnimal((Animal) imageRec.getUserData());
//                        mapRec.getTrackedAnimal().setOffspring(true);
//                        mapRec.setTrackedAnimalChildren(0);
//                    }
//                });
//                imageFol.setOnMouseClicked(event -> {
//                    if (imageFol.getUserData() != null && imageFol.getUserData().getClass().equals(Animal.class) && startButtonFol.getText().equals("start")){
//                        mapFol.setTrackedAnimal((Animal) imageFol.getUserData());
//                        mapFol.getTrackedAnimal().setOffspring(true);
//                        mapFol.setTrackedAnimalChildren(0);
//                    }
//                });
//            }
//        }
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
        VBox plotsRec = new VBox(statRec.plotAnimals(),statRec.plotGrass(),statRec.plotEnergy(),statRec.plotLifeTime(),statRec.plotChildren());
        VBox plotsFol = new VBox(statFol.plotAnimals(),statFol.plotGrass(),statFol.plotEnergy(),statFol.plotLifeTime(),statFol.plotChildren());
        Thread engineThreadRec = new Thread(engineRec);
        Thread engineThreadFol = new Thread(engineFol);
        engineThreadRec.start();
        engineThreadFol.start();
        return new Scene(new HBox(plotsRec,new VBox(new Label("Dominant genotype"), new HBox(startButtonRec, statRec.getDominantGenotypeLabel()), gridRec, highlightRec, statRec.trackedStats()),
                new VBox(new Label("Dominant genotype"), new HBox(startButtonFol, statFol.getDominantGenotypeLabel()), gridFol, highlightFol, statFol.trackedStats()), plotsFol), 1300, 1000);
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
        TextField heightTxt = new TextField("20");
        TextField widthTxt = new TextField("20");
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
        CheckBox magicRec = new CheckBox();
        CheckBox magicFol = new CheckBox();
        magicRec.setText("Should rectangular map be magic?");
        magicFol.setText("Should folded map be magic?");

        button.setOnAction(event -> {
            mapRec = new RectangularMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()), magicRec.isSelected());
            mapFol = new FoldedMap(Integer.parseInt(widthTxt.getText()), Integer.parseInt(heightTxt.getText()),
                    Double.parseDouble(jungleRatioTxt.getText()), Integer.parseInt(startEnergyTxt.getText()),
                    Integer.parseInt(moveEnergyTxt.getText()), Integer.parseInt(plantEnergyTxt.getText()),
                    Integer.parseInt(startingAnimalsTxt.getText()), magicFol.isSelected());
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

        VBox menu = new VBox(mapProperties, height, width, jungleRatio,new HBox(magicRec, magicFol), mapElements, startEnergy, moveEnergy, plantEnergy, startingAnimals, button);
        return new Scene(menu, 600, 250);
    }
}
