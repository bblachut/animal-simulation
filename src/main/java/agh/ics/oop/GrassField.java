package agh.ics.oop;

import java.util.*;
import java.util.HashMap;


public class GrassField extends AbstractWorldMap implements IWorldMap{

    private final int grassQuantity;
    private final HashMap<Vector2d, Grass> grassOnMap = new HashMap<>();
    private final MapBoundary mapBoundary = new MapBoundary();

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
                    mapBoundary.add(position, Grass.class);
                }
            }
        }
        Vector2d position = new Vector2d(10, 10);
        mapBoundary.add(position, Grass.class);
        grassOnMap.put(position, new Grass(position));
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);
        if (object != null){
            return object;
        }
        return grassOnMap.get(position);
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException{
        if(super.place(animal)){
            animal.addObserver(mapBoundary);
            mapBoundary.add(animal.getPosition(), Animal.class);
            return true;
        }
        throw new IllegalArgumentException("position " +animal.getPosition() + " is already occupied");
    }

    @Override
    public Vector2d getLowerLeft() {
        return mapBoundary.getLowerLeft();
    }

    @Override
    public Vector2d getUpperRight() {
        return mapBoundary.getUpperRight();
    }

    public HashMap<Vector2d, Grass> getGrass() {
        return grassOnMap;
    }
}
