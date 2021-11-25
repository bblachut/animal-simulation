package agh.ics.oop;

public class RectangularMap extends AbstractWorldMap implements IWorldMap {
    private  final Vector2d v1 = new Vector2d(0,0);
    private  final Vector2d v2;
    public RectangularMap(int width, int height) {
        v2 = new Vector2d(width-1, height-1);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(v2) && position.follows(v1) && super.canMoveTo(position);
    }

    @Override
    protected Vector2d getLowerLeft() {
        return v1;
    }

    @Override
    protected Vector2d getUpperRight() {
        return v2;
    }

}
