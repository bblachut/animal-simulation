package agh.ics.oop;

import java.util.Arrays;

public class OptionsParser {
    public MoveDirection[] parse(String[] dirs) throws IllegalArgumentException{
        MoveDirection[] res = new MoveDirection[dirs.length];
        int counter = 0;
        for (int i = 0; i < res.length; i++) {
            switch (dirs[i]) {
                case "f", "forward" -> {
                    res[counter] = MoveDirection.FORWARD;
                    counter++;
                }
                case "b", "backward" -> {
                    res[counter] = MoveDirection.BACKWARD;
                    counter++;
                }
                case "l", "left" -> {
                    res[counter] = MoveDirection.LEFT;
                    counter++;
                }
                case "r", "right" -> {
                    res[counter] = MoveDirection.RIGHT;
                    counter++;
                }
                default -> throw new IllegalArgumentException(dirs[i] + " is not legal move specification");
            }
        }
        return Arrays.copyOfRange(res, 0, counter);
    }
}
