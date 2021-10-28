package agh.ics.oop;

import java.util.Arrays;

public class OptionsParser {
    public MoveDirection[] parse(String[] dirs){
        MoveDirection[] res = new MoveDirection[dirs.length];
        int counter = 0;
        for (int i = 0; i < res.length; i++) {
            switch (dirs[i]){
                case "f": case  "forward":
                    res[i] = MoveDirection.FORWARD;
                    counter++;
                    break;
                case "b": case "backward":
                    res[i] = MoveDirection.BACKWARD;
                    counter++;
                    break;
                case "l": case "left":
                    res[i] = MoveDirection.LEFT;
                    counter++;
                    break;
                case "r": case "right":
                    res[i] = MoveDirection.RIGHT;
                    counter++;
                    break;
                default:
                    break;
            }
        }
        return Arrays.copyOfRange(res, 0, counter);
    }
}
