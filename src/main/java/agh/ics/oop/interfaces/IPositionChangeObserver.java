package agh.ics.oop.interfaces;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);

}
