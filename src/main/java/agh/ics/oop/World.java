package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        Animal animal = new Animal();
        OptionsParser optionsParser = new OptionsParser();
        MoveDirection[] directions = optionsParser.parse(new String[]{"f", "forward", "right"});
        for (MoveDirection dir : directions) {
            animal.move(dir);
        }
        System.out.println(animal);
    }

    static void run(Direction[] directions){
        for(Direction dir : directions) {
            System.out.println(switch (dir) {
                case FORWARD -> "Zwierzak idzie do przodu";
                case BACKWARD -> "Zwierzak idzie do tyłu";
                case RIGHT -> "Zwierzak skręca w prawo";
                case LEFT -> "Zwierzak skręca w lewo";
            });
        }
    }
    static Direction[] convert(String[] args){
        Direction[] dirs = new Direction[args.length];
        for (int i = 0; i < args.length; i++){
            switch (args[i]) {
                case "f" -> dirs[i] = Direction.FORWARD;
                case "b" -> dirs[i] = Direction.BACKWARD;
                case "l" -> dirs[i] = Direction.LEFT;
                case "r" -> dirs[i] = Direction.RIGHT;
            }
        }
        return dirs;
    }
}
