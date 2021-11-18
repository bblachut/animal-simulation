package agh.ics.oop;

import java.util.HashMap;

public abstract class AbstractWorldMap implements IPositionChangeObserver{
//    protected ArrayList<Animal> animals = new ArrayList<>();
    protected HashMap<Vector2d, Animal> animals = new HashMap<>();
    public abstract boolean canMoveTo(Vector2d position);
    public abstract Object objectAt(Vector2d position);

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(), animal);
            return true;
        }
        return false;
    }

    public boolean isOccupied(Vector2d position) {
        Object object = objectAt(position);
        return object != null;
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        Animal animal = animals.remove(oldPosition);
        animals.put(newPosition, animal);
    }
}
