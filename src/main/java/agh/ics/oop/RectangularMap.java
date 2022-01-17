package agh.ics.oop;


import javafx.scene.image.ImageView;

public class RectangularMap extends AbstractWorldMap {  // czy ta klasa jest jeszcze potrzebna?
    public RectangularMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int startingAnimals, boolean isMagic, ImageView[][] imagesArray) {
        super(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, startingAnimals, isMagic, imagesArray);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRight) && position.follows(lowerLeft);
    }
}
