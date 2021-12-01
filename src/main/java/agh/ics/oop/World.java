package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        try {
            MoveDirection[] directions = new OptionsParser().parse(args);
            IWorldMap map = new GrassField(10);
            System.out.println(map.objectAt(new Vector2d(0, 0)));
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
            IEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
            System.out.println(map);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
}
