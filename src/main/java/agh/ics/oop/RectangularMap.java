package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;

public class RectangularMap implements IWorldMap {
    private final int width;
    private final int height;
//    HashMap<Vector2d, Animal> map = new HashMap<>();
    ArrayList<Animal> animals = new ArrayList<>();

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canMoveTo(Vector2d position) {
        return (0 <= position.x) && (position.x < width) && (0 <= position.y) && (position.y < height) && !isOccupied(position);    // nie powinno się schodzić na niższe poziomy abstrakcji
    }

    public boolean place(Animal animal) {
        if (!isOccupied(animal.getLocation())){ // czy ten warunek jest poprawny?
            animals.add(animal);
            return true;
        }
        return false;
    }

    public boolean isOccupied(Vector2d position) {  // ta metoda jest łudząco podobna do tej niżej
        for (Animal animal:animals) {
            if (animal.getLocation().equals(position)){
                return true;
            }
        }
        return false;
    }

    public Object objectAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getLocation().equals(position)){
                return animal;
            }
        }
        return null;
    }


    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);  // nowy obiekt co wywołanie
        return mapVisualizer.draw(new Vector2d(0,0), new Vector2d(width-1, height-1));
    }
}
