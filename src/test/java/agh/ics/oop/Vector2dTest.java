package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    void testEquals(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(1, 1);
        Vector2d v3 = new Vector2d(1, -11);
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));
        assertFalse(v1.equals(v3));
        assertFalse(v3.equals(v1));
    }

    @Test
    void testToString(){
        Vector2d v1 = new Vector2d(1, 1);
        assertEquals("(1,1)", v1.toString());
    }

    @Test
    void testPrecedes(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(2, 4);
        Vector2d v4 = new Vector2d(5, 1);
        Vector2d v5 = new Vector2d(5, 1);
        assertTrue(v1.precedes(v2));
        assertTrue(v1.precedes(v3));
        assertTrue(v2.precedes(v3));
        assertTrue(v5.precedes(v4));
        assertTrue(v4.precedes(v5));
        assertFalse(v3.precedes(v4));
        assertFalse(v2.precedes(v4));
        assertFalse(v2.precedes(v1));
    }

    @Test
    void testFollows(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(2, 4);
        Vector2d v4 = new Vector2d(5, 1);
        Vector2d v5 = new Vector2d(5, 1);
        assertTrue(v2.follows(v1));
        assertTrue(v3.follows(v2));
        assertTrue(v3.follows(v1));
        assertTrue(v4.follows(v5));
        assertTrue(v5.follows(v4));
        assertFalse(v1.follows(v2));
        assertFalse(v2.follows(v3));
        assertFalse(v1.follows(v3));
        assertFalse(v4.follows(v3));
    }

    @Test
    void testUpperRight(){
        Vector2d v1 = new Vector2d(0, 2);
        Vector2d v2 = new Vector2d(2, 0);
        Vector2d v3 = new Vector2d(2, 2);
        Vector2d v4 = new Vector2d(2, 2);

        assertEquals(v3, v1.upperRight(v2));
        assertEquals(v3, v2.upperRight(v1));
        assertEquals(v4, v2.upperRight(v3));
        assertEquals(v4, v4.upperRight(v3));
    }

    @Test
    void testLowerLeft(){
        Vector2d v1 = new Vector2d(0, 2);
        Vector2d v2 = new Vector2d(2, 0);
        Vector2d v3 = new Vector2d(2, 2);
        Vector2d v4 = new Vector2d(2, 2);
        Vector2d v5 = new Vector2d(0, 0);

        assertEquals(v5, v1.lowerLeft(v2));
        assertEquals(v5, v2.lowerLeft(v1));
        assertEquals(v3, v3.lowerLeft(v4));
        assertEquals(v5, v1.lowerLeft(v5));
    }

    @Test
    void testAdd(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 4);
        assertEquals(v3, v1.add(v2));
        assertEquals(v3, v2.add(v1));
    }
    @Test
    void testSubstract(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 4);
        assertEquals(v1, v3.subtract(v2));
    }

    @Test
    void testOpposite(){
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(-1, -1);
        Vector2d v3 = new Vector2d(-1, 1);
        Vector2d v4 = new Vector2d(0, 0);
        assertEquals(v2, v1.opposite());
        assertEquals(v4, v4.opposite());
        assertEquals(v1, v2.opposite());
    }
}
