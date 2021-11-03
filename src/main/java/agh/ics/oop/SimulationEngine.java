package agh.ics.oop;

import java.util.ArrayList;

public class SimulationEngine implements IEngine{
    private final MoveDirection[] moveDirections;
    private final IWorldMap map;
    private final Vector2d[] startingLocations;
    private ArrayList<Animal> arrayOfAnimals = new ArrayList<>();

    public SimulationEngine(MoveDirection[] moveDirections, IWorldMap map, Vector2d[] startingLocations){
        this.map = map;
        this.moveDirections = moveDirections;
        this.startingLocations = startingLocations;
        this.addAnimalsToMap();
    }

    private void addAnimalsToMap(){
        for (Vector2d startingLocation : startingLocations) {
            Animal animal = new Animal(map, startingLocation);
            if (!map.place(new Animal(map, startingLocation))) {
                System.out.println("Zajęte pole :CC");
            }else arrayOfAnimals.add(animal);
        }
    }

    public void run(){
        int counter = 0;
        while (counter < moveDirections.length){
            for(Animal animal : arrayOfAnimals){
                animal.move(moveDirections[counter]);
                counter++;
            }
        }
    }
}
