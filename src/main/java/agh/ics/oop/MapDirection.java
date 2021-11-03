package agh.ics.oop;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    Vector2d[] vectors = {new Vector2d(0,1), new Vector2d(-1,0), new Vector2d(0,-1), new Vector2d(1,0)};
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case EAST ->"E";
            case SOUTH ->"S";
            case WEST ->"W";
        };
    }
    public MapDirection next(){
        return switch (this){
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }
    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> vectors[0];
            case WEST -> vectors[1];
            case SOUTH -> vectors[2];
            case EAST -> vectors[3];
        };
    }
}
