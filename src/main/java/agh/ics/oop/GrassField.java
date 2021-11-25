package agh.ics.oop;

import java.util.*;
import java.util.HashMap;


public class GrassField extends AbstractWorldMap implements IWorldMap{

    private final int grassQuantity;
    private Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
                    upperRight = upperRight.upperRight(position);
                    lowerLeft = lowerLeft.lowerLeft(position);
                }
            }
        }
    }

    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);
        if (object != null){
            return object;
        }
        return grassOnMap.get(position);
    }

    @Override
    protected Vector2d getLowerLeft() {//if grass position also change, add calculating its lower left too
        for (Animal animal : animals.values()) {
            lowerLeft = lowerLeft.lowerLeft(animal.getPosition());
        }
        return lowerLeft;
    }

    @Override
    protected Vector2d getUpperRight() {
        for (Animal animal : animals.values()) {
            upperRight = upperRight.upperRight(animal.getPosition());
        }
        return upperRight;
    }
}
