package agh.ics.oop;

public class Animal {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d location = new Vector2d(2,2);
    private IWorldMap map;

    public Animal(){}   // co to tu robi?

    public Animal(IWorldMap map){
        this.map = map;
    }
    public Animal(IWorldMap map, Vector2d initialPosition){
        location = initialPosition;
        this.map = map;
    }

    public Vector2d getLocation() {
        return location;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setLocation(Vector2d location) {
        this.location = location;   // teleport
    }

    @Override
    public String toString(){
        return orientation.toString();
    }

    public void move(MoveDirection direction){
        switch (direction){
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> {
                Vector2d x = location.add(orientation.toUnitVector());  // czy x to czytelna nazwa?
                if(map.canMoveTo(x)){
                    location = x;
                }else System.out.println("zajemte pole " + x);
            }
            case BACKWARD -> {
                Vector2d x = location.add(orientation.toUnitVector().opposite());
                if(map.canMoveTo(x)){
                    location = x;
                }else System.out.println("zajemte pole " + x);
            }
        }
    }
}
