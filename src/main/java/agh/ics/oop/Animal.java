package agh.ics.oop;

public class Animal {
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d location = new Vector2d(2,2);
    @Override
    public String toString(){
        return "Położenie to: "+location+", zwierzę idzie w kierunku " + mapDirection;
    }
    public void move(MoveDirection direction){
        switch (direction){
            case RIGHT -> mapDirection = mapDirection.next();
            case LEFT -> mapDirection = mapDirection.previous();
            case FORWARD -> {
                Vector2d x = location.add(mapDirection.toUnitVector());
                if(x.follows(new Vector2d(0,0)) && x.precedes(new Vector2d(4,4))){
                location = x;
                }
            }
            case BACKWARD -> {
                Vector2d x = location.add(mapDirection.toUnitVector().opposite());
                if (x.follows(new Vector2d(0, 0)) && x.precedes(new Vector2d(4, 4))) {
                    location = x;
                }
            }
        }
    }
}
