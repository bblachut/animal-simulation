package agh.ics.oop;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class GrassField extends AbstractWorldMap implements IWorldMap{

    private final int grassQuantity;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private final ArrayList<Grass> grassOnMap = new ArrayList<>();

    public GrassField(int grassQuantity){
        this.grassQuantity = grassQuantity;
        addGrass();
    }

    public void addGrass(){
        Set<Vector2d> picked = new HashSet<>();
        Random rng = new Random();
        for (int i = 0; i < grassQuantity; i++) {
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = rng.nextInt((int) (Math.sqrt(10*grassQuantity)));
                int y = rng.nextInt((int) (Math.sqrt(10*grassQuantity)));
                Vector2d position = new Vector2d(x, y);
                if (!picked.contains(position)){
                    shouldShuffle = false;
                    picked.add(position);
                    grassOnMap.add(new Grass(position));
                    maxX = max(maxX, x);
                    minX = min(minX, x);
                    minY = min(minY, y);
                    maxY = max(maxY, y);
                }
            }
        }
        System.out.println(picked);
    }

    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    public Object objectAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getLocation().equals(position)){
                return animal;
            }
        }
        for (Grass grass: grassOnMap) {
            if (grass.getPosition().equals(position)){
                return grass;
            }
        }
        return null;
    }
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        for (Animal animal:animals) {
            maxX = max(maxX, animal.getLocation().x);
            minX = min(minX, animal.getLocation().x);
            maxY = max(maxY, animal.getLocation().y);
            minY = min(minY, animal.getLocation().y);
        }
        return mapVisualizer.draw(new Vector2d(minX,minY), new Vector2d(maxX, maxY));
    }
}
