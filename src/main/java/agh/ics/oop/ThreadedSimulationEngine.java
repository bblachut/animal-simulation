package agh.ics.oop;

import java.util.ArrayList;

public class ThreadedSimulationEngine implements IEngine, Runnable{
    private final MoveDirection[] moveDirections;
    private final IWorldMap map;
    private final ArrayList<Animal> arrayOfAnimals = new ArrayList<>();
    private final ArrayList<IEngineObserver> observersList = new ArrayList<>();

    public ThreadedSimulationEngine(MoveDirection[] moveDirections, IWorldMap map, Vector2d[] startingLocations) throws IllegalArgumentException{
        this.map = map;
        this.moveDirections = moveDirections;
        addAnimalsToMap(startingLocations);
    }

    private void addAnimalsToMap(Vector2d[] startingLocations) throws IllegalArgumentException{
        for (Vector2d startingLocation : startingLocations) {
            Animal animal = new Animal(map, startingLocation);
            map.place(animal);
            arrayOfAnimals.add(animal);
        }
    }


    public void addObserver(IEngineObserver observer){
        observersList.add(observer);
    }

    public void run(){
        int counter = 0;
        while (counter < moveDirections.length){
            for(Animal animal : arrayOfAnimals){
                if (counter < moveDirections.length) {
                    animal.move(moveDirections[counter]);
                    for (IEngineObserver observer:observersList) {
                        observer.moved();
                    }
                    counter++;
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}