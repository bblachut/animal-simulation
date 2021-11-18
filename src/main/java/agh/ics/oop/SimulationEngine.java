package agh.ics.oop;

import java.util.ArrayList;

public class SimulationEngine implements IEngine{
    // brak testów
    private final MoveDirection[] moveDirections;
    private final IWorldMap map;
    private ArrayList<Animal> arrayOfAnimals = new ArrayList<>();

    public SimulationEngine(MoveDirection[] moveDirections, IWorldMap map, Vector2d[] startingLocations){
        this.map = map;
        this.moveDirections = moveDirections;
        addAnimalsToMap(startingLocations);
    }

    private void addAnimalsToMap(Vector2d[] startingLocations){
        for (Vector2d startingLocation : startingLocations) {
            Animal animal = new Animal(map, startingLocation);
            if (!map.place(animal)) {
                System.out.println("Zajęte pole :CC");
            }else arrayOfAnimals.add(animal);
        }
    }

    public void run(){
        int counter = 0;
        while (counter < moveDirections.length){
            for(Animal animal : arrayOfAnimals){
                if (counter < moveDirections.length) {
                    animal.move(moveDirections[counter]);
                    counter++;
                }
            }
        }
    }
}
