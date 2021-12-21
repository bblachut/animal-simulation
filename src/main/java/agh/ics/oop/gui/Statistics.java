package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.interfaces.IEngineObserver;
import agh.ics.oop.ThreadedSimulationEngine;
import javafx.application.Platform;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics implements IEngineObserver {
    private int dayCounter = 1;
    private final AbstractWorldMap map;
    private final XYChart.Series<Number, Number> animals = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> grass = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> energy = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> lifetime = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> children = new XYChart.Series<>();
    private final Label dominantGenotypeLabel = new Label();
    private final Label trackedOffspring = new Label();
    private final Label trackedChildren = new Label();
    private final Label dayOfDeath = new Label();
    private final  ArrayList<String> statsToSave = new ArrayList<>();
    private double averageAnimalAmount = 0;
    private double averageGrassAmount = 0;
    private double averageAverageEnergy = 0;
    private double averageAverageLifetime = 0;
    private double averageAverageChildren = 0;


    public Statistics(AbstractWorldMap map, ThreadedSimulationEngine engine){
        this.map = map;
        engine.addObserver(this);
    }

    public LineChart<Number, Number> plotAnimals(){
        return plot(animals);
    }

    public LineChart<Number, Number> plotGrass(){
        LineChart<Number, Number> lineChart = plot(grass);
        grass.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0FBA19");
        return lineChart;
    }

    public LineChart<Number, Number> plotEnergy(){
        LineChart<Number, Number> lineChart = plot(energy);
        energy.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #921AB9");
        return lineChart;
    }

    public LineChart<Number, Number> plotLifeTime(){
        LineChart<Number, Number> lineChart = plot(lifetime);
        lifetime.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FF0000");
        return lineChart;
    }

    public LineChart<Number, Number> plotChildren(){
        LineChart<Number, Number> lineChart = plot(children);
        children.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FFF300");
        return lineChart;
    }

    private LineChart<Number, Number> plot(XYChart.Series<Number, Number> series){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.getData().add(series);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setPrefHeight(100);
        lineChart.setPrefWidth(100);
        return lineChart;
    }

    public VBox trackedStats(){
        trackedChildren.setText("Animal has 0 children");
        trackedOffspring.setText("Animal has 0 descendants");
        dayOfDeath.setText("");
        return new VBox(trackedOffspring, trackedChildren, dayOfDeath);
    }

    public Label getDominantGenotypeLabel(){
        return dominantGenotypeLabel;
    }

    public void updateTrackedStats(){
        if (map.getTrackedAnimal() != null){
            Animal tracked = map.getTrackedAnimal();
            trackedChildren.setText("Animal has "+map.getTrackedAnimalChildren()+" children");
            trackedOffspring.setText("Animal has "+map.getOffspring()+ " descendants");
            if (tracked.getCurrentEnergy()<=0 && dayOfDeath.getText().equals("")){
                dayOfDeath.setText("Animal died at day "+dayCounter);
            }else if (tracked.getCurrentEnergy()>0){
                dayOfDeath.setText("");
            }
        }
    }

    private void addDailyStats(){
        String stats = dayCounter+"   "+ map.getAnimalsAmount() + "," + map.getGrassAmount() + "," + Math.round(map.getAverageEnergy()*100.0)/100.0 + ","
                + Math.round(map.getAverageLifetime()*100.0)/100.0 + "," + Math.round(map.getAverageChildrenAmount()*100.0)/100.0;
        averageAnimalAmount = Math.round((averageAnimalAmount*(dayCounter-1)/dayCounter + (double) map.getAnimalsAmount()/dayCounter)*100.0)/100.0;
        averageGrassAmount = Math.round((averageGrassAmount*(dayCounter-1)/dayCounter + (double) map.getGrassAmount()/dayCounter)*100.0)/100.0;
        averageAverageEnergy = Math.round((averageAverageEnergy*(dayCounter-1)/dayCounter + map.getAverageEnergy()/dayCounter)*100.0)/100.0;
        averageAverageLifetime = Math.round((averageAverageLifetime*(dayCounter-1)/dayCounter + map.getAverageLifetime()/dayCounter)*100.0)/100.0;
        averageAverageChildren = Math.round((averageAverageChildren*(dayCounter-1)/dayCounter + map.getAverageChildrenAmount()/dayCounter)*100.0)/100.0;
        statsToSave.add(stats);
    }

    public void savetoFile(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("simulationStats.txt", false));
            for (String str: statsToSave) {
                writer.append(str);
                writer.newLine();
            }
            writer.append(String.valueOf(averageAnimalAmount)).append(",").append(String.valueOf(averageGrassAmount))//average values
                    .append(",").append(String.valueOf(averageAverageEnergy)).append(",")
                    .append(String.valueOf(averageAverageLifetime)).append(",").append(String.valueOf(averageAverageChildren));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dayFinished() {
        Platform.runLater(() -> {
            animals.getData().add(new XYChart.Data<>(dayCounter, map.getAnimalsAmount()));
            grass.getData().add(new XYChart.Data<>(dayCounter, map.getGrassAmount()));
            energy.getData().add(new XYChart.Data<>(dayCounter, Math.round(map.getAverageEnergy()*100.0)/100.0));
            lifetime.getData().add(new XYChart.Data<>(dayCounter, Math.round(map.getAverageLifetime()*100.0)/100.0));
            children.getData().add(new XYChart.Data<>(dayCounter, Math.round(map.getAverageChildrenAmount()*100.0)/100.0));
            dominantGenotypeLabel.setText(map.getDominantGenotype());
            updateTrackedStats();
            addDailyStats();
            dayCounter++;
        });
    }
}
