package agh.ics.oop;

import agh.ics.oop.interfaces.IMapElement;
import agh.ics.oop.interfaces.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement {
    private int orientation;    // enum byłby czytelniejszy
    private Vector2d position;
    private final AbstractWorldMap map;
    private final Genotype genotype;
    private final ArrayList<IPositionChangeObserver> observersList = new ArrayList<>();
    private int currentEnergy;
    private int lifetime = 0;
    private int childrenAmount = 0;
    private boolean isOffspring = false; //says if the animal is a descendant of the tracked animal

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
            case 4 -> { // za miesiąc będzie Pan pamiętał, dlaczego akurat 4?
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

    public Animal makeChild(Animal other){
        int childEnergy = this.getCurrentEnergy()/4+ other.getCurrentEnergy()/4;    // czemu rodzice nie tracą tej energii?
        Genotype childGenotype = new Genotype(this, other);
        childrenAmount++;
        other.childrenAmount++;
        Animal child = new Animal(map, position, childGenotype, childEnergy);
        if (isOffspring || other.isOffspring){
            child.setOffspring(true);
        }
        return child;
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
