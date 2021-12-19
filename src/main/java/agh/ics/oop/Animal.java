package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement{
    private int orientation;
    private Vector2d position;
    private final AbstractWorldMap map;
    private final Genotype genotype;
    private final ArrayList<IPositionChangeObserver> observersList = new ArrayList<>();
    private int currentEnergy;
    private int lifetime = 0;
    private int childrenAmount = 0;
    private boolean isOffspring = false;

    public Animal(AbstractWorldMap map, Vector2d initialPosition, Genotype genotype, int startEnergy){
        position = initialPosition;
        this.map = map;
        this.genotype = genotype;
        orientation = ThreadLocalRandom.current().nextInt(0, 8);
        currentEnergy = startEnergy;
    }

    public void move(){
        lifetime++;
        int orientationChange = genotype.getArray()[ThreadLocalRandom.current().nextInt(0, 32)];
        switch (orientationChange){
            case 0 -> {
                Vector2d oldPos = position;
                Vector2d destination = position.add(map.getMoveVector(orientation));
                if (map.canMoveTo(destination)) {
                    destination = new Vector2d(Math.floorMod(destination.x, map.getWidth()), Math.floorMod(destination.y, map.getHeight()));
                    position = destination;
                    positionChanged(oldPos, destination);
                }
            }
            case 4 -> {
                Vector2d oldPos = position;
                Vector2d destination = position.add(map.getMoveVector((orientation+4)%8));
                if (map.canMoveTo(destination)) {
                    destination = new Vector2d(Math.floorMod(destination.x, map.getWidth()), Math.floorMod(destination.y, map.getHeight()));
                    position = destination;
                    positionChanged(oldPos, destination);
                }
            }
            default -> orientation = (orientation+orientationChange)%8;
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
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void changeCurrentEnergy(int energyChange) {
        currentEnergy += energyChange;
    }

    public Animal makeChild(Animal other){
        int childEnergy = this.getCurrentEnergy()/4+ other.getCurrentEnergy()/4;
        Genotype childGenotype = new Genotype(this, other);
        childrenAmount++;
        other.incrementChildren();
        Animal child = new Animal(map, position, childGenotype, childEnergy);
        if (isOffspring || other.isOffspring){
            child.setOffspring(true);
        }
        return child;
    }

    public int getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getLifetime(){
        return lifetime;
    }

    public int getChildrenAmount(){
        return childrenAmount;
    }

    public void setOffspring(boolean bool){
        isOffspring = bool;
    }

    public boolean getIsOffspring(){
        return isOffspring;
    }

    public void incrementChildren(){
        childrenAmount++;
    }
}
