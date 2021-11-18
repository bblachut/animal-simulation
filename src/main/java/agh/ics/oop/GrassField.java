package agh.ics.oop;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.HashMap;


public class GrassField extends AbstractWorldMap implements IWorldMap{

    private final int grassQuantity;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private final HashMap<Vector2d, Grass> grassOnMap = new HashMap<>();

    public GrassField(int grassQuantity){
        this.grassQuantity = grassQuantity;
        addGrass();
    }

    private void addGrass(){
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
                    grassOnMap.put(position, new Grass(position));
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
        Animal animal = animals.get(position);
        if (animal != null){
            return animal;
        }
        return grassOnMap.get(position);
    }
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        for (Animal animal: animals.values()) {
            maxX = max(maxX, animal.getPosition().x);
            minX = min(minX, animal.getPosition().x);
            maxY = max(maxY, animal.getPosition().y);
            minY = min(minY, animal.getPosition().y);
        }
        return mapVisualizer.draw(new Vector2d(minX,minY), new Vector2d(maxX, maxY));
    }
}
