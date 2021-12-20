package agh.ics.oop;

import javafx.scene.image.ImageView;

public class FoldedMap extends AbstractWorldMap{
    public FoldedMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int startingAnimals, boolean isMagic, ImageView[][] imagesArray) {
        super(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, startingAnimals, isMagic, imagesArray);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }


}
