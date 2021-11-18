package agh.ics.oop;

import java.util.ArrayList;

public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2,2);
    private final IWorldMap map;
    private ArrayList<IPositionChangeObserver> observersList = new ArrayList<>();

//    public Animal(){}

    public Animal(IWorldMap map){
        this.map = map;
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
                    positionChanged(position, destination);
                }else System.out.println("zajemte pole " + destination);
            }
            case BACKWARD -> {
                Vector2d destination = position.add(orientation.toUnitVector().opposite());
                if(map.canMoveTo(destination)){
                    positionChanged(position, destination);
                }else System.out.println("zajemte pole " + destination);
            }
        }
    }
    void addObserver(IPositionChangeObserver observer){
        observersList.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observersList.remove(observer);
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observersList){
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
