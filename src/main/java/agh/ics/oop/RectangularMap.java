package agh.ics.oop;

public class RectangularMap extends AbstractWorldMap implements IWorldMap {
    private final int width;
    private final int height;
    private  final Vector2d v1 = new Vector2d(0,0);
    private  final Vector2d v2;
    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
        v2 = new Vector2d(width-1, height-1);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(v2) && position.follows(v1) && !isOccupied(position);
    }

    public Object objectAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getLocation().equals(position)){
                return animal;
            }
        }
        return null;
    }
    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(v1, v2);
    }
}
