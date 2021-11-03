package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTests {

    @Test
    void testObjectAt(){
        RectangularMap map = new RectangularMap(10, 6);
        Vector2d v1 = new Vector2d(2,5);
        Vector2d v2 = new Vector2d(2,6);
        Animal animal = new Animal(map, v1);
        map.place(animal);
        assertEquals(animal, map.objectAt(v1));
        assertTrue(map.isOccupied(v1));
        animal.move(MoveDirection.FORWARD);
        assertFalse(map.isOccupied(v1));
        assertEquals(animal, map.objectAt(v2));
        assertNull(map.objectAt(v1));
        assertTrue(map.isOccupied(v2));

    }
    @Test
    void testCanMoveTo(){
        RectangularMap map = new RectangularMap(10, 6);
        Vector2d v1 = new Vector2d(2,5);
        Vector2d v2 = new Vector2d(3,6);
        Animal animal1 = new Animal(map, v1);
        Animal animal2 = new Animal(map, v2);
        map.place(animal1);
        map.place(animal2);
        assertFalse(map.canMoveTo(new Vector2d(-1, 2)));
        assertFalse(map.canMoveTo(new Vector2d(11, 2)));
        assertFalse(map.canMoveTo(v1));
        assertFalse(map.canMoveTo(new Vector2d(10, 7)));
        assertTrue(map.canMoveTo(new Vector2d(10, 6)));
        animal1.move(MoveDirection.FORWARD);
        assertTrue(map.canMoveTo(v1));

    }
}
