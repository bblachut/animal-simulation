package agh.ics.oop;

import java.util.HashMap;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected HashMap<Vector2d, Animal> animals = new HashMap<>();
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    public boolean place(Animal animal) throws IllegalArgumentException{
        if (canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(), animal);
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException("position " +animal.getPosition() + " is already occupied");
    }

    public boolean isOccupied(Vector2d position) {
        Object object = objectAt(position);
        return object != null;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        Animal animal = animals.remove(oldPosition);
        animals.put(newPosition, animal);
    }

    public Object objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(getLowerLeft(), getUpperRight());
    }

    protected abstract Vector2d getLowerLeft();
    protected abstract Vector2d getUpperRight();
}

