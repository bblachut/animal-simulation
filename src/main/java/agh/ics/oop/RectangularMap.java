package agh.ics.oop;


public class RectangularMap extends AbstractWorldMap {
    public RectangularMap(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int startingAnimals) {
        super(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, startingAnimals);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(v2) && position.follows(v1);
    }


    @Override
    public Vector2d getLowerLeft() {
        return v1;
    }

    @Override
    public Vector2d getUpperRight() {
        return v2;
    }

}
