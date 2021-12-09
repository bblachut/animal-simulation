package agh.ics.oop;

import java.util.ArrayList;

public class Animal implements IMapElement{
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observersList = new ArrayList<>();
    public Animal(IWorldMap map){
        this(map, new Vector2d(2,2));
    }
    public Animal(IWorldMap map, Vector2d initialPosition){
        position = initialPosition;
        this.map = map;
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    @Override
    public String toString(){
        return orientation.toString();
    }

    public void move(MoveDirection direction){
        switch (direction){
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> {
                Vector2d destination = position.add(orientation.toUnitVector());
                if(map.canMoveTo(destination)){
                    Vector2d oldPos = position;
                    position = destination;
                    positionChanged(oldPos, destination);
                }
            }
            case BACKWARD -> {
                Vector2d destination = position.add(orientation.toUnitVector().opposite());
                if(map.canMoveTo(destination)){
                    Vector2d oldPos = position;
                    position = destination;
                    positionChanged(oldPos, destination);
                }
            }
        }
    }
    public void addObserver(IPositionChangeObserver observer){
        observersList.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer){
        observersList.remove(observer);
    }
    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observersList){
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
