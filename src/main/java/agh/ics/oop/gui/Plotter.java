package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.IEngineObserver;
import agh.ics.oop.ThreadedSimulationEngine;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Plotter implements IEngineObserver {
    private int dayCounter = 1;
    private AbstractWorldMap map;
    private XYChart.Series animals;
    private XYChart.Series grass;
    private XYChart.Series energy;
    private XYChart.Series lifetime;
    private XYChart.Series children;

    public Plotter(AbstractWorldMap map, ThreadedSimulationEngine engine){
        this.map = map;
        engine.addObserver(this);
    }

    public LineChart plotAnimals(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        animals = new XYChart.Series();
        animals.setName("animals");
        lineChart.getData().add(animals);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        return lineChart;
    }

    public LineChart plotGrass(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        grass = new XYChart.Series();
        grass.setName("Grass");
        lineChart.getData().add(grass);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        grass.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #0FBA19");
//        lineChart.lookup(".chart-legend-item-symbol").setStyle("-fx-background-color: #0FBA19, white;");
        return lineChart;
    }

    public LineChart plotEnergy(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        energy = new XYChart.Series();
        energy.setName("Average energy");
        lineChart.getData().add(energy);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        grass.getData().add(new XYChart.Data(0, 0));
        energy.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #921AB9");
//        grass.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #921AB9, #921AB9;");
        return lineChart;
    }

    public LineChart plotLifeTime(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        lifetime = new XYChart.Series();
        lifetime.setName("Average lifetime");
        lineChart.getData().add(lifetime);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lifetime.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FF0000");
//        grass.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #FF0000, #FF0000;");
        return lineChart;
    }

    public LineChart plotChildren(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart lineChart = new LineChart(xAxis,yAxis);
        children = new XYChart.Series();
        children.setName("children");
        lineChart.getData().add(children);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        children.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #FFF300");
//        grass.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: #FFF300, #FFF300;");
        return lineChart;
    }

    @Override
    public void dayFinished() {
        dayCounter++;
        Platform.runLater(() -> {
            animals.getData().add(new XYChart.Data(dayCounter, map.getAnimalsAmount()));
            grass.getData().add(new XYChart.Data(dayCounter, map.getGrassAmount()));
            energy.getData().add(new XYChart.Data(dayCounter, map.getEnergySum()));
            lifetime.getData().add(new XYChart.Data(dayCounter, map.getAverageLifetime()));
            children.getData().add(new XYChart.Data(dayCounter, map.getAnimalsAmount()));
        });
    }
}
