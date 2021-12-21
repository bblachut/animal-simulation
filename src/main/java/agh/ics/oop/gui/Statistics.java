package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.interfaces.IEngineObserver;
import agh.ics.oop.ThreadedSimulationEngine;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics implements IEngineObserver {
    private int dayCounter = 1;
    private final AbstractWorldMap map;
    private XYChart.Series animals;
    private XYChart.Series grass;
    private XYChart.Series energy;
    private XYChart.Series lifetime;
    private XYChart.Series children;
    private final Label dominantGenotypeLabel = new Label();
    private Label trackedOffspring = new Label();
    private Label trackedChildren = new Label();
    private Label dayOfDeath = new Label();
    private ArrayList<String> statsToSave = new ArrayList<>();
    private double averageAnimalAmount = 0;
    private double averageGrassAmount = 0;
    private double averageAverageEnergy = 0;
    private double averageAverageLifetime = 0;
    private double averageAverageChildren = 0;


    public Statistics(AbstractWorldMap map, ThreadedSimulationEngine engine){
        this.map = map;
        engine.addObserver(this);
    }

    public LineChart plotAnimals(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        animals = new XYChart.Series();
        lineChart.getData().add(animals);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setPrefHeight(100);
        lineChart.setPrefWidth(100);
        return lineChart;
    }

    public LineChart plotGrass(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        grass = new XYChart.Series();
        lineChart.getData().add(grass);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        grass.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0FBA19");
        lineChart.setPrefHeight(100);
        lineChart.setPrefWidth(100);
        return lineChart;
    }

    public LineChart plotEnergy(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        energy = new XYChart.Series();
        lineChart.getData().add(energy);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        grass.getData().add(new XYChart.Data(0, 0));
        energy.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #921AB9");
        lineChart.setPrefHeight(100);
        lineChart.setPrefWidth(100);
        return lineChart;
    }

    public LineChart plotLifeTime(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        lifetime = new XYChart.Series();
        lineChart.getData().add(lifetime);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lifetime.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FF0000");
        lineChart.setPrefHeight(100);
        lineChart.setPrefWidth(100);
        return lineChart;
    }

    public LineChart plotChildren(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        children = new XYChart.Series();
        lineChart.getData().add(children);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        children.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FFF300");
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
        String stats = map.getAnimalsAmount() + "," + map.getGrassAmount() + "," + Math.round(map.getAverageEnergy()*100.0)/100.0 + ","
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
            writer.append(averageAnimalAmount+","+averageGrassAmount+","+averageAverageEnergy+","+averageAverageLifetime+","+averageAverageChildren);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dayFinished() {
        Platform.runLater(() -> {
            animals.getData().add(new XYChart.Data(dayCounter, map.getAnimalsAmount()));
            grass.getData().add(new XYChart.Data(dayCounter, map.getGrassAmount()));
            energy.getData().add(new XYChart.Data(dayCounter, Math.round(map.getAverageEnergy()*100.0)/100.0));
            lifetime.getData().add(new XYChart.Data(dayCounter, Math.round(map.getAverageLifetime()*100.0)/100.0));
            children.getData().add(new XYChart.Data(dayCounter, Math.round(map.getAverageChildrenAmount()*100.0)/100.0));
            dominantGenotypeLabel.setText(map.getDominantGenotype());
            updateTrackedStats();
            addDailyStats();
        });
        dayCounter++;

    }
}
