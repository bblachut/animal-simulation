package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.IEngineObserver;
import agh.ics.oop.ThreadedSimulationEngine;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    @Override
    public void dayFinished() {
        dayCounter++;
        Platform.runLater(() -> {
            animals.getData().add(new XYChart.Data(dayCounter, map.getAnimalsAmount()));
            grass.getData().add(new XYChart.Data(dayCounter, map.getGrassAmount()));
            energy.getData().add(new XYChart.Data(dayCounter, map.getEnergySum()));
            lifetime.getData().add(new XYChart.Data(dayCounter, map.getAverageLifetime()));
            children.getData().add(new XYChart.Data(dayCounter, map.getAverageChildrenAmount()));
            dominantGenotypeLabel.setText(map.getDominantGenotype());
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
        });
    }
}
