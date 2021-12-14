package agh.ics.oop;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    private final Vector2d jungleLL;
    private final Vector2d jungleUR;
    private final float jungleRatio;
    private final int width;
    private final int height;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final Set<Vector2d> steppeFreeSquares = new HashSet<Vector2d>();
    private final Set<Vector2d> jungleFreeSquares = new HashSet<Vector2d>();
    protected Vector2d[] moveVectors = {new Vector2d(0,1), new Vector2d(1,1),new Vector2d(1,0), new Vector2d(1,-1),
            new Vector2d(0,-1),new Vector2d(-1,-1),new Vector2d(-1,0),new Vector2d(-1,1),};
    protected HashMap<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected HashMap<Vector2d, Grass> grass = new HashMap<>();

    public AbstractWorldMap(int width, int height, float jungleRatio1, int startEnergy1, int moveEnergy1, int plantEnergy1, int startingAnimals) {
        this.jungleRatio = jungleRatio1;
        this.startEnergy = startEnergy1;
        this.moveEnergy = moveEnergy1;
        this.plantEnergy = plantEnergy1;
        this.width = width;
        this.height = height;
        jungleLL = new Vector2d((int)((width/2)-((width*Math.sqrt(height*jungleRatio1))/(2*height))), (int)((height/2)-(Math.sqrt(height*jungleRatio1)/2)));
        jungleUR = new Vector2d((int)((width/2)+((width*Math.sqrt(height*jungleRatio1))/(2*height))), (int)((height/2)+(Math.sqrt(height*jungleRatio1)/2)));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d v = new Vector2d(x, y);
                addToFreeSquares(v);
            }
        }
        placeStartingAnimals(startingAnimals);
    }

    private void placeStartingAnimals(int startingAnimals){
        Random rng = new Random();
        for (int i = 0; i < startingAnimals; i++) {
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = rng.nextInt(width);
                int y = rng.nextInt(height);
                Vector2d position = new Vector2d(x, y);
                if (jungleFreeSquares.contains(position) || steppeFreeSquares.contains(position)){
                    shouldShuffle = false;
                    removeFromFreeSquares(position);
                    int[] randomGenotype = new int[32];
                    for (int j = 0; j < 32; j++) {
                        randomGenotype[i] = rng.nextInt(8);
                    }
                    this.place(new Animal(this, position, randomGenotype, startEnergy));
                }
            }
        }
    }


    public void place(Animal animal){
        if (canMoveTo(animal.getPosition())){
            if(!animals.containsKey(animal.getPosition())){
                animals.put(animal.getPosition(), new ArrayList<Animal>());
                removeFromFreeSquares(animal.getPosition());
            }
            animals.get(animal.getPosition()).add(animal);
            animal.addObserver(this);
        }
    }

    public void remove(Animal animal){
        Vector2d position = animal.getPosition();
        animals.get(position).remove(animal);
        if (animals.get(position).isEmpty()) {
            animals.remove(position);
            addToFreeSquares(position);
        }
        animal.removeObserver(this);
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition){
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).isEmpty()){
            animals.remove(oldPosition);
            addToFreeSquares(oldPosition);
        }
        if(!animals.containsKey(animal.getPosition())){
            animals.put(animal.getPosition(), new ArrayList<Animal>());
            removeFromFreeSquares(newPosition);
        }
        animals.get(animal.getPosition()).add(animal);
    }

    public void addGrass(){
        if (!jungleFreeSquares.isEmpty()){
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = ThreadLocalRandom.current().nextInt(jungleLL.x, jungleUR.x+1);
                int y = ThreadLocalRandom.current().nextInt(jungleLL.y, jungleUR.y+1);
                Vector2d position = new Vector2d(x, y);
                if (jungleFreeSquares.contains(position)){
                    shouldShuffle = false;
                    jungleFreeSquares.remove(position);
                }
            }
        }
        if(!steppeFreeSquares.isEmpty()){
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = ThreadLocalRandom.current().nextInt(0,width-1);
                int y = ThreadLocalRandom.current().nextInt(0,width+1);
                Vector2d position = new Vector2d(x, y);
                if (steppeFreeSquares.contains(position)){
                    shouldShuffle = false;
                    steppeFreeSquares.remove(position);
                }
            }
        }
    }

    public void animalsEat(){
        for (Vector2d position: grass.keySet()) {
            ArrayList<Animal> list = animals.get(position);
            list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
            int maxEnergy = list.get(0).getCurrentEnergy();
            int maxEnergyAnimals = 0;
            for (Animal animal: list) {
                if (animal.getCurrentEnergy() == maxEnergy){
                    maxEnergyAnimals++;
                }
            }
            for (int i = 0; i < maxEnergyAnimals; i++) {
                list.get(i).changeCurrentEnergy(plantEnergy/maxEnergyAnimals);
            }
        }
    }

    public void animalsBreed(){
        for (ArrayList<Animal> list : animals.values()){
            if (list.size() >= 2){
                list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
                if (list.get(0).getCurrentEnergy()>=startEnergy/2 && list.get(1).getCurrentEnergy()>=startEnergy/2){
                    list.get(0).makeChild(list.get(1));
                }
            }
        }
    }

    public void animalsMove(){
        for (ArrayList<Animal> list : animals.values()){
            for (Animal animal:list){
                animal.move();
            }
        }
    }

    public void removeDead(){
        for (ArrayList<Animal> list : animals.values()){
            for (Animal animal:list){
                if(animal.getCurrentEnergy()<=0){
                    remove(animal);
                }
            }
        }
    }

    private void removeFromFreeSquares(Vector2d position){
        if (jungleFreeSquares.contains(position) || steppeFreeSquares.contains(position)) {
            if (position.precedes(jungleUR) && position.follows(jungleLL)) {
                jungleFreeSquares.remove(position);
            } else {
                steppeFreeSquares.remove(position);
            }
        }
    }

    private void addToFreeSquares(Vector2d position){
        if (position.precedes(jungleUR) && position.follows(jungleLL)) {
            jungleFreeSquares.add(position);
        } else {
            steppeFreeSquares.add(position);
        }
    }

    public HashMap<Vector2d, ArrayList<Animal>> getAnimals(){
        return animals;
    }

    public Vector2d getMoveVector(int orientation){
        return moveVectors[orientation];
    }

    public abstract Vector2d getLowerLeft();

    public abstract Vector2d getUpperRight();

    public abstract boolean canMoveTo(Vector2d position);
}

