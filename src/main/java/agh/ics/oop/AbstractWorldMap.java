package agh.ics.oop;

import agh.ics.oop.gui.GuiElementBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMap implements IPositionChangeObserver{
    private final Vector2d jungleLL;
    private final Vector2d jungleUR;
    private final int width;
    private final int height;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private ImageView[][] imagesArray;
    protected final Vector2d v1 = new Vector2d(0,0);
    protected final Vector2d v2;
    private final Set<Vector2d> steppeFreeSquares = new HashSet<>();
    private final Set<Vector2d> jungleFreeSquares = new HashSet<>();
    Set<Animal> livingAnimals = new HashSet<>();
    protected Vector2d[] moveVectors = {new Vector2d(0,1), new Vector2d(1,1),new Vector2d(1,0), new Vector2d(1,-1),
            new Vector2d(0,-1),new Vector2d(-1,-1),new Vector2d(-1,0),new Vector2d(-1,1),};
    protected ConcurrentHashMap<Vector2d, ArrayList<Animal>> animals = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<Vector2d, Grass> grass = new ConcurrentHashMap<>();

    public AbstractWorldMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int startingAnimals) {
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.width = width;
        this.height = height;
        v2 = new Vector2d(width-1, height-1);
        jungleLL = new Vector2d((int)((width/2)-((width*Math.sqrt((height*height)*jungleRatio))/(2*height))), (int)((height/2)-(Math.sqrt((height*height)*jungleRatio)/2)));
        jungleUR = new Vector2d((int)((width/2)+((width*Math.sqrt((height*height)*jungleRatio))/(2*height))), (int)((height/2)+(Math.sqrt((height*height)*jungleRatio)/2)));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d v = new Vector2d(x, y);
                addToFreeSquares(v);
            }
        }
        imagesArray = new ImageView[width][height];
        setUpImageArray();
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
                        randomGenotype[j] = rng.nextInt(8);
                    }
                    Animal animal = new Animal(this, position, randomGenotype, startEnergy);
                    place(animal);
                    livingAnimals.add(animal);
                }
            }
        }
    }

    public void place(Animal animal){
        if (canMoveTo(animal.getPosition())){
            if(!animals.containsKey(animal.getPosition())){
                animals.put(animal.getPosition(), new ArrayList<>());
                removeFromFreeSquares(animal.getPosition());
            }
            animals.get(animal.getPosition()).add(animal);
            animal.addObserver(this);
        }
        updateImage(animal.getPosition());
    }

    public void remove(Animal animal){
        Vector2d position = animal.getPosition();
        animals.get(position).remove(animal);
        if (animals.get(position).isEmpty()) {
            animals.remove(position);
            addToFreeSquares(position);
        }
        animal.removeObserver(this);
        updateImage(animal.getPosition());
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition){
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).isEmpty()){
            animals.remove(oldPosition);
            addToFreeSquares(oldPosition);
        }
        if(!animals.containsKey(animal.getPosition())){
            animals.put(animal.getPosition(), new ArrayList<>());
            removeFromFreeSquares(newPosition);
        }
        animals.get(animal.getPosition()).add(animal);
        updateImage(newPosition);
        updateImage(oldPosition);
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
                    grass.put(position, new Grass(position));
                    updateImage(position);
                }
            }
        }
        if(!steppeFreeSquares.isEmpty()){
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = ThreadLocalRandom.current().nextInt(0,width);
                int y = ThreadLocalRandom.current().nextInt(0,width);
                Vector2d position = new Vector2d(x, y);
                if (steppeFreeSquares.contains(position)){
                    shouldShuffle = false;
                    steppeFreeSquares.remove(position);
                    grass.put(position, new Grass(position));
                    updateImage(position);
                }
            }
        }
    }

    public void animalsEat(){
        Iterator<Map.Entry<Vector2d, Grass>> it = grass.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Vector2d, Grass> pair = it.next();
            ArrayList<Animal> list = animals.get(pair.getKey());
            if (list != null) {
                it.remove();
                list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
                int maxEnergy = list.get(0).getCurrentEnergy();
                int maxEnergyAnimals = 0;
                for (Animal animal : list) {
                    if (animal.getCurrentEnergy() == maxEnergy) {
                        maxEnergyAnimals++;
                    }
                }
                for (int i = 0; i < maxEnergyAnimals; i++) {
                    list.get(i).changeCurrentEnergy(plantEnergy / maxEnergyAnimals);
                }
            }
        }
    }

    public void animalsBreed(){
        for (ArrayList<Animal> list : animals.values()){
            if (list.size() >= 2){
                list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
                if (list.get(0).getCurrentEnergy()>=startEnergy/2 && list.get(1).getCurrentEnergy()>=startEnergy/2){
                    Animal child = list.get(0).makeChild(list.get(1));
                    place(child);
                    livingAnimals.add(child);
                }
            }
        }
    }

    public void animalsMove(){
        for (Animal animal:livingAnimals){
            animal.move();
            animal.changeCurrentEnergy(-moveEnergy);
            updateImage(animal.getPosition());
        }
    }

    public void removeDead(){
        Iterator<Animal> it = livingAnimals.iterator();
        while (it.hasNext()){
            Animal animal = it.next();
            if(animal.getCurrentEnergy()<=0){
                remove(animal);
                it.remove();
            }
        }
        System.out.println(livingAnimals.size());
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

    private void setUpImageArray(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                try {
                    ImageView image = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\transparent.png")));
                    image.setFitWidth(18);
                    image.setFitHeight(18);
                    imagesArray[x][y] = image;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateImage(Vector2d position){
        GuiElementBox guiElementBox = new GuiElementBox();
            if (animals.containsKey(position)){
                ArrayList<Animal> list = animals.get(position);
                list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
                imagesArray[position.x][position.y].setImage(guiElementBox.getBoxElement(list.get(0)));
            }else if (grass.containsKey(position)){
                imagesArray[position.x][position.y].setImage(guiElementBox.getBoxElement(grass.get(position)));
            }else {
                try {
                    imagesArray[position.x][position.y].setImage(new Image(new FileInputStream(".\\src\\main\\resources\\transparent.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }

    public ConcurrentHashMap<Vector2d, ArrayList<Animal>> getAnimals(){
        return animals;
    }

    public ConcurrentHashMap<Vector2d, Grass> getGrass() {
        return grass;
    }

    public Vector2d getMoveVector(int orientation){
        return moveVectors[orientation];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageView[][] getImagesArray(){
        return imagesArray;
    }

    public abstract Vector2d getLowerLeft();

    public abstract Vector2d getUpperRight();

    public abstract boolean canMoveTo(Vector2d position);

}

