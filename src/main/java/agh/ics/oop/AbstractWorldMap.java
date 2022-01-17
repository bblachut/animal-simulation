package agh.ics.oop;

import agh.ics.oop.gui.GuiElementBox;
import agh.ics.oop.interfaces.IPositionChangeObserver;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMap implements IPositionChangeObserver { // ta klasa robi zdecydowanie za dużo
    private final Vector2d jungleLL;
    private final Vector2d jungleUR;
    private final int width;
    private final int height;
    private final int startEnergy;  // czy to powinno być pole mapy?
    private final int moveEnergy;
    private final int plantEnergy;
    protected final Vector2d lowerLeft = new Vector2d(0, 0);
    protected final Vector2d upperRight;
    private final Set<Vector2d> steppeFreeSquares = new HashSet<>();
    private final Set<Vector2d> jungleFreeSquares = new HashSet<>();
    private final Set<Animal> livingAnimals = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Vector2d[] moveVectors = {new Vector2d(0, 1), new Vector2d(1, 1), new Vector2d(1, 0), new Vector2d(1, -1),
            new Vector2d(0, -1), new Vector2d(-1, -1), new Vector2d(-1, 0), new Vector2d(-1, 1),}; // czy to powinno być pole mapy?
    private final ConcurrentHashMap<Vector2d, ArrayList<Animal>> animals = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Vector2d, Grass> grass = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Genotype, Integer> numberOfGenotypes = new ConcurrentHashMap<>();   // czy to powinno być pole mapy?

    private double averageLifetime = 0;
    private double averageChildrenAmount = 0;
    private int deadAnimalsCounter = 0;
    private Animal trackedAnimal;
    private int trackedAnimalChildren;
    private int trackedAnimalDescendents;

    private final boolean isMagic;
    private int magicDone = 0;

    private final ImageView[][] imagesArray;
    private Image transparent;
    private Image redSquare;
    private final GuiElementBox guiElementBox = new GuiElementBox();

    {
        try {
            transparent = new Image(new FileInputStream(".\\src\\main\\resources\\transparent.png"));
            redSquare = new Image(new FileInputStream(".\\src\\main\\resources\\highlighter.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();    // czy bez tych obrazków da się prawidłowo działać?
        }
    }

    public AbstractWorldMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy,
                            int startingAnimals, boolean isMagic, ImageView[][] imagesArray) throws IllegalArgumentException {
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.width = width;
        this.height = height;
        this.isMagic = isMagic;
        upperRight = new Vector2d(width - 1, height - 1);
        if (jungleRatio < 0 || jungleRatio > 1) {
            throw new IllegalArgumentException("Jungle ratio must be between 0 and 1");
        }
        jungleLL = new Vector2d((int) ((width / 2) - ((width * Math.sqrt((height * height) * jungleRatio)) / (2 * height))),
                (int) ((height / 2) - (Math.sqrt((height * height) * jungleRatio) / 2)));
        jungleUR = new Vector2d((int) ((width / 2) + ((width * Math.sqrt((height * height) * jungleRatio)) / (2 * height))),
                (int) ((height / 2) + (Math.sqrt((height * height) * jungleRatio) / 2)));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d v = new Vector2d(x, y);
                addToFreeSquares(v);
            }
        }
        this.imagesArray = imagesArray;
        placeStartingAnimals(startingAnimals);
    }

    private void placeStartingAnimals(int startingAnimals) throws IllegalArgumentException {    // kompilator ignoruje tę deklarację throws
        if (startingAnimals > width * height) {
            throw new IllegalArgumentException("Too many starting animals");
        }
        Random rng = new Random();
        for (int i = 0; i < startingAnimals; i++) {
            while (true) {
                int x = rng.nextInt(width);
                int y = rng.nextInt(height);
                Vector2d position = new Vector2d(x, y);
                if (jungleFreeSquares.contains(position) || steppeFreeSquares.contains(position)) {
                    removeFromFreeSquares(position);
                    Animal animal = new Animal(this, position, new Genotype(), startEnergy);
                    place(animal);
                    livingAnimals.add(animal);
                    break;
                }
            }
        }
    }

    public void place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            averageChildrenAmount = averageChildrenAmount * livingAnimals.size() / (livingAnimals.size() + 1) + 0.0 / (livingAnimals.size() + 1);
            if (!animals.containsKey(animal.getPosition())) {
                animals.put(animal.getPosition(), new ArrayList<>());
                removeFromFreeSquares(animal.getPosition());
            }
            animals.get(animal.getPosition()).add(animal);
            Genotype genotype = animal.getGenotype();
            if (numberOfGenotypes.containsKey(genotype)) {
                numberOfGenotypes.put(genotype, numberOfGenotypes.get(genotype) + 1);
            } else {
                numberOfGenotypes.put(genotype, 1);
            }
            animal.addObserver(this);
        }
        updateImage(animal.getPosition());
    }

    private void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.get(position).remove(animal);
        if (animals.get(position).isEmpty()) {
            animals.remove(position);
            addToFreeSquares(position);
        }
        Genotype genotype = animal.getGenotype();
        numberOfGenotypes.put(genotype, numberOfGenotypes.get(genotype) - 1);
        if (numberOfGenotypes.get(genotype) < 1) {
            numberOfGenotypes.remove(genotype);
        }
        animal.removeObserver(this);
        updateImage(animal.getPosition());
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).isEmpty()) {
            animals.remove(oldPosition);
            addToFreeSquares(oldPosition);
        }
        if (!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>());
            removeFromFreeSquares(newPosition);
        }
        animals.get(animal.getPosition()).add(animal);
        updateImage(newPosition);
        updateImage(oldPosition);
    }

    public void addGrass() {
        if (!jungleFreeSquares.isEmpty()) {
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = ThreadLocalRandom.current().nextInt(jungleLL.x, jungleUR.x + 1);
                int y = ThreadLocalRandom.current().nextInt(jungleLL.y, jungleUR.y + 1);
                Vector2d position = new Vector2d(x, y);
                if (jungleFreeSquares.contains(position)) {
                    shouldShuffle = false;
                    jungleFreeSquares.remove(position);
                    grass.put(position, new Grass(position));
                    updateImage(position);
                }
            }
        }
        if (!steppeFreeSquares.isEmpty()) {
            boolean shouldShuffle = true;
            while (shouldShuffle) {
                int x = ThreadLocalRandom.current().nextInt(0, width);
                int y = ThreadLocalRandom.current().nextInt(0, width);
                Vector2d position = new Vector2d(x, y);
                if (steppeFreeSquares.contains(position)) {
                    shouldShuffle = false;
                    steppeFreeSquares.remove(position);
                    grass.put(position, new Grass(position));
                    updateImage(position);
                }
            }
        }
    }

    public void animalsEat() {
        Iterator<Map.Entry<Vector2d, Grass>> it = grass.entrySet().iterator();
        while (it.hasNext()) {
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

    public void animalsBreed() {
        for (ArrayList<Animal> list : animals.values()) {
            if (list.size() >= 2) {
                list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
                if (list.get(0).getCurrentEnergy() >= startEnergy / 2 && list.get(1).getCurrentEnergy() >= startEnergy / 2) {
                    Animal child = list.get(0).makeChild(list.get(1));
                    if (list.get(0) == trackedAnimal || list.get(1) == trackedAnimal) {
                        trackedAnimalChildren++;
                    }
                    if (list.get(0).getIsOffspring() || list.get(1).getIsOffspring()) {
                        trackedAnimalDescendents++;
                    }
                    averageChildrenAmount += 2.0 / livingAnimals.size();
                    place(child);
                    livingAnimals.add(child);
                }
            }
        }
        doMagic();
    }

    public void animalsMove() {
        for (Animal animal : livingAnimals) {
            animal.move();
            animal.changeCurrentEnergy(-moveEnergy);
            updateImage(animal.getPosition());
        }
    }

    public void removeDead() {
        Iterator<Animal> it = livingAnimals.iterator();
        while (it.hasNext()) {
            Animal animal = it.next();
            if (animal.getCurrentEnergy() <= 0) {
                averageLifetime = averageLifetime * deadAnimalsCounter / (deadAnimalsCounter + 1) + (double) animal.getLifetime() / (deadAnimalsCounter + 1);
                averageChildrenAmount = (averageChildrenAmount - (double) animal.getChildrenAmount() / livingAnimals.size()) * (double) livingAnimals.size() / (livingAnimals.size() - 1);
                deadAnimalsCounter++;
                removeAnimal(animal);
                it.remove();
            }
        }
        doMagic();
    }

    private void removeFromFreeSquares(Vector2d position) {
        if (jungleFreeSquares.contains(position)) {
            jungleFreeSquares.remove(position);
        } else if (steppeFreeSquares.contains(position)) {
            steppeFreeSquares.remove(position);
        }
    }

    private void addToFreeSquares(Vector2d position) {
        if (position.precedes(jungleUR) && position.follows(jungleLL)) {
            jungleFreeSquares.add(position);
        } else {
            steppeFreeSquares.add(position);
        }
    }

    //magic

    private void doMagic() {
        if (isMagic && magicDone < 3 && livingAnimals.size() == 5) {
            magicDone++;
            Random rng = new Random();
            for (Animal animal : livingAnimals) {
                if (livingAnimals.size() < width * height) {
                    boolean shouldShuffle = true;
                    while (shouldShuffle) {
                        int x = rng.nextInt(width);
                        int y = rng.nextInt(height);
                        Vector2d position = new Vector2d(x, y);
                        if (!animals.containsKey(position)) {
                            shouldShuffle = false;
                            removeFromFreeSquares(position);
                            Animal animalCopy = new Animal(this, position, animal.getGenotype(), startEnergy);
                            place(animalCopy);
                            livingAnimals.add(animalCopy);
                        }
                    }
                }
            }
        }
    }


    //displaying

    private void updateImage(Vector2d position) {
        if (animals.containsKey(position)) {
            ArrayList<Animal> list = animals.get(position);
            list.sort(Comparator.comparingInt((Animal a) -> -a.getCurrentEnergy()));
            Animal animal = list.get(0);
            Platform.runLater(() -> {
                imagesArray[position.x][position.y].setImage(guiElementBox.getBoxElement(animal));
                imagesArray[position.x][position.y].setOpacity((double) animal.getCurrentEnergy() / (2 * startEnergy));
                imagesArray[position.x][position.y].setUserData(animal);
            });
        } else if (grass.containsKey(position)) {
            Grass grass1 = grass.get(position);
            Platform.runLater(() -> {
                imagesArray[position.x][position.y].setImage(guiElementBox.getBoxElement(grass1));
                imagesArray[position.x][position.y].setOpacity(1);
                imagesArray[position.x][position.y].setUserData(grass1);
            });
        } else {
            Platform.runLater(() -> {
                imagesArray[position.x][position.y].setImage(transparent);
                imagesArray[position.x][position.y].setUserData(null);
            });
        }
    }

    public void highlight() {
        for (Animal animal : livingAnimals) {
            if (animal.getGenotype().equals(Collections.max(numberOfGenotypes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey())) {
                Platform.runLater(() -> imagesArray[animal.getPosition().x][animal.getPosition().y].setImage(redSquare));
            }
        }
    }


    //getters and setters

    public int getOffspring() {
        return trackedAnimalDescendents;
    }

    public Vector2d getMoveVector(int orientation) {
        return moveVectors[orientation];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public int getAnimalsAmount() {
        return livingAnimals.size();
    }

    public int getGrassAmount() {
        return grass.size();
    }

    public double getAverageEnergy() {
        int energySum = 0;
        for (Animal animal : livingAnimals) {
            if (animal.getCurrentEnergy() > 0) {
                energySum += animal.getCurrentEnergy();
            }
        }
        return (double) energySum / livingAnimals.size();
    }

    public double getAverageLifetime() {
        return averageLifetime;
    }

    public double getAverageChildrenAmount() {
        return averageChildrenAmount;
    }

    public String getDominantGenotype() {
        return Collections.max(numberOfGenotypes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).toString();
    }

    public Animal getTrackedAnimal() {  // tym też się zajmuje mapa?
        return trackedAnimal;
    }

    public void setTrackedAnimal(Animal animal) {
        trackedAnimal = animal;
    }

    public int getTrackedAnimalChildren() {
        return trackedAnimalChildren;
    }

    public void setTrackedAnimalChildren(int trackedAnimalChildren) {
        this.trackedAnimalChildren = trackedAnimalChildren;
    }

    public void clearOffspring() {
        trackedAnimalDescendents = 0;
        for (Animal animal : livingAnimals) {
            animal.setOffspring(false);
        }
    }

    //abstract

    public abstract boolean canMoveTo(Vector2d position);

}

