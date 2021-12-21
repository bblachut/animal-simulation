package agh.ics.oop;

import agh.ics.oop.interfaces.IMapElement;

public class Grass implements IMapElement {
    private final Vector2d position;
    public Grass(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return "*";
    }
}
