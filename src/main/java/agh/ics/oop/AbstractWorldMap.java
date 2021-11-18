package agh.ics.oop;

import java.util.ArrayList;

public abstract class AbstractWorldMap{
    protected ArrayList<Animal> animals = new ArrayList<>();
    public abstract boolean canMoveTo(Vector2d position);
    public abstract Object objectAt(Vector2d position);

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getLocation())){
            animals.add(animal);
            return true;
        }
        return false;
    }

    public boolean isOccupied(Vector2d position) {
        Object object = objectAt(position);
        return object != null;
    }

}
