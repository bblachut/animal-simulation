package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement{
    private int orientation;
    private Vector2d position;
    private final AbstractWorldMap map;
    private final int[] genotype;
    private final ArrayList<IPositionChangeObserver> observersList = new ArrayList<>();
    private int currentEnergy;

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int[] genotype, int startEnergy){
        position = initialPosition;
        this.map = map;
        Arrays.sort(genotype);
        this.genotype = genotype;
        orientation = ThreadLocalRandom.current().nextInt(0, 8);
        currentEnergy = startEnergy;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void move(){
        int orientationChange = genotype[ThreadLocalRandom.current().nextInt(0, 32)];
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

    public int[] getGenotype() {
        return genotype;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void changeCurrentEnergy(int energyChange) {
        currentEnergy += energyChange;
    }

    public Animal makeChild(Animal other){
        boolean strongerTakesLeft = new Random().nextBoolean();
        Animal a1 = this;
        Animal a2 = other;
        int childEnergy = getCurrentEnergy()/4+ other.getCurrentEnergy()/4;
        changeCurrentEnergy(-getCurrentEnergy()/4);
        other.changeCurrentEnergy(-other.getCurrentEnergy()/4);
        int[] childGenotype = new int[32];
        if (currentEnergy < other.getCurrentEnergy()) {
            a1 = other;
            a2 = this;
        }
        int[] a1Gen = a1.getGenotype();
        int[] a2Gen = a2.getGenotype();
        for (int i = 0; i < 32; i++) {
            if (strongerTakesLeft){
                if (i<a1.getCurrentEnergy()*32/a2.getCurrentEnergy()) {
                    childGenotype[i] = a1Gen[i];
                }else {
                    childGenotype[i] = a2Gen[i];
                }
            }else {
                if (i<32-(a1.getCurrentEnergy()*32/a2.getCurrentEnergy())) {
                    childGenotype[i] = a2Gen[i];
                }else {
                    childGenotype[i] = a1Gen[i];
                }
            }
        }
       return new Animal(map, position, childGenotype, childEnergy);
    }

    public int getOrientation() {
        return orientation;
    }
}
