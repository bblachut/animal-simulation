package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver{
    Comparator<Vector2d> compareVectorByX = Comparator.comparingInt((Vector2d v) -> v.x).thenComparingInt(v -> v.y);
    Comparator<Vector2d> compareVectorByY = Comparator.comparingInt((Vector2d v) -> v.y).thenComparingInt(v -> v.x);
    private final SortedSet<Vector2d> xAxis = new TreeSet<>(compareVectorByX);
    private final SortedSet<Vector2d> yAxis = new TreeSet<>(compareVectorByY);

    public void add(Vector2d v){
        xAxis.add(v);
        yAxis.add(v);
    }

    public void remove(Vector2d v){
        xAxis.remove(v);
        yAxis.remove(v);
    }

     public Vector2d getLowerLeft(){
        return xAxis.first().lowerLeft(yAxis.first());
     }

    public Vector2d getUpperRight(){
        return xAxis.last().upperRight(yAxis.last());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        xAxis.remove(oldPosition);
        xAxis.add(newPosition);
        yAxis.remove(oldPosition);
        yAxis.add(newPosition);
    }
}
