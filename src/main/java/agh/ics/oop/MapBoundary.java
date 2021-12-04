package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.util.Pair;

public class MapBoundary implements IPositionChangeObserver{
    Comparator<Pair<Vector2d, Class>> compareVectorByX = Comparator.comparingInt((Pair<Vector2d, Class> v) -> v.getKey().x)
            .thenComparingInt(v -> v.getKey().y).thenComparing(v -> v.getValue().getCanonicalName());
    Comparator<Pair<Vector2d, Class>> compareVectorByY = Comparator.comparingInt((Pair<Vector2d, Class> v) -> v.getKey().y)
            .thenComparingInt(v -> v.getKey().x).thenComparing(v -> v.getValue().getCanonicalName());
    private final SortedSet<Pair<Vector2d, Class>> xAxis = new TreeSet<>(compareVectorByX);
    private final SortedSet<Pair<Vector2d, Class>> yAxis = new TreeSet<>(compareVectorByY);

    public void add(Vector2d v, Class c){
        xAxis.add(new Pair<>(v,c));
        yAxis.add(new Pair<>(v,c));
    }

    public void remove(Vector2d v, Class c){
        xAxis.remove(new Pair<>(v,c));
        yAxis.remove(new Pair<>(v,c));
    }

     public Vector2d getLowerLeft(){
        return xAxis.first().getKey().lowerLeft(yAxis.first().getKey());
     }

    public Vector2d getUpperRight(){
        System.out.println(xAxis);
        return xAxis.last().getKey().upperRight(yAxis.last().getKey());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        xAxis.remove(new Pair<>(oldPosition, Animal.class));
        xAxis.add(new Pair<>(newPosition, Animal.class));
        yAxis.remove(new Pair<>(oldPosition, Animal.class));
        yAxis.add(new Pair<>(newPosition, Animal.class));
    }
}
