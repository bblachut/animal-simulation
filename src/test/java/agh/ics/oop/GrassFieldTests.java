package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTests {
    @Test
    void testCanMoveTo(){
        GrassField map = new GrassField(10);
        Vector2d v1 = new Vector2d(2,5);
        Vector2d v2 = new Vector2d(3,6);
        Animal animal1 = new Animal(map, v1);
        Animal animal2 = new Animal(map, v2);
        map.place(animal1);
        map.place(animal2);
        assertTrue(map.canMoveTo(new Vector2d(-1, 2)));
        assertTrue(map.canMoveTo(new Vector2d(11, 2)));
        assertFalse(map.canMoveTo(v1));
        assertFalse(map.canMoveTo(v2));
        animal1.move(MoveDirection.FORWARD);
        assertTrue(map.canMoveTo(v1));
    }
    @Test
    void testObjectAt(){
        GrassField map = new GrassField(10);
        Vector2d v1 = new Vector2d(2,5);
        Animal animal = new Animal(map, v1);
        map.place(animal);
        assertEquals(animal, map.objectAt(v1));
    }
}
