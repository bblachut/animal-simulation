package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {
//    @Test
//    void testOrientation(){
//        Animal animal = new Animal();
//        assertEquals(MapDirection.NORTH, animal.getOrientation());
//        animal.move(MoveDirection.RIGHT);
//        assertEquals(MapDirection.EAST, animal.getOrientation());
//        animal.move(MoveDirection.LEFT);
//        assertEquals(MapDirection.NORTH, animal.getOrientation());
//        animal.move(MoveDirection.RIGHT);
//        animal.move(MoveDirection.RIGHT);
//        animal.move(MoveDirection.RIGHT);
//        assertEquals(MapDirection.WEST, animal.getOrientation());
//        animal.move(MoveDirection.LEFT);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//        animal.move(MoveDirection.BACKWARD);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(MapDirection.SOUTH, animal.getOrientation());
//    }
//    @Test
//    void testLocation(){
//        Animal animal = new Animal();
//        assertEquals(new Vector2d(2,2), animal.getLocation());
//        animal.move(MoveDirection.RIGHT);
//        assertEquals(new Vector2d(2,2), animal.getLocation());
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(3,2), animal.getLocation());
//        animal.move(MoveDirection.LEFT);
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(3,3), animal.getLocation());
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(3,4), animal.getLocation());
//        animal.move(MoveDirection.LEFT);
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(2,4), animal.getLocation());
//    }
//    @Test
//    void testBorders(){
//        Animal animal = new Animal();
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(2, 4), animal.getLocation());
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        assertEquals(new Vector2d(2, 0), animal.getLocation());
//        animal.move(MoveDirection.LEFT);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        animal.move(MoveDirection.FORWARD);
//        assertEquals(new Vector2d(0, 0), animal.getLocation());
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        animal.move(MoveDirection.BACKWARD);
//        assertEquals(new Vector2d(4, 0), animal.getLocation());
//    }
//    @Test
//    void testParser(){
//        OptionsParser optionsParser = new OptionsParser();
//        assertArrayEquals(new MoveDirection[] {}, optionsParser.parse(new String[] {}));
//        assertArrayEquals(new MoveDirection[] {}, optionsParser.parse(new String[] {"sd", "dwa", ""}));
//        assertArrayEquals(new MoveDirection[] {MoveDirection.FORWARD}, optionsParser.parse(new String[] {"f"}));
//        assertArrayEquals(new MoveDirection[] {MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.RIGHT}, optionsParser.parse(new String[] {"f", "backward", "r"}));
//        assertArrayEquals(new MoveDirection[] {MoveDirection.LEFT, MoveDirection.LEFT, MoveDirection.RIGHT}, optionsParser.parse(new String[] {"left", "l", "rff", "right"}));
//        assertArrayEquals(new MoveDirection[] {MoveDirection.BACKWARD, MoveDirection.FORWARD}, optionsParser.parse(new String[] {"b", "forward", "foward"}));
//    }
}
