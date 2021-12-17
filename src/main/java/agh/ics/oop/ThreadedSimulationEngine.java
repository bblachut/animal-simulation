package agh.ics.oop;


import java.util.ArrayList;

public class ThreadedSimulationEngine implements Runnable{
    private final AbstractWorldMap map;
    private boolean shouldRun = true;
    private final ArrayList<IEngineObserver> observersList = new ArrayList<>();

    public ThreadedSimulationEngine(AbstractWorldMap map) throws IllegalArgumentException{
        this.map = map;
    }


    public void run(){
        while (true) {
            if (shouldRun) {
                map.removeDead();
                map.animalsMove();
                map.animalsEat();
                map.animalsBreed();
                map.addGrass();
                for (IEngineObserver observer:observersList) {
                    observer.dayFinished();
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    public void addObserver(IEngineObserver observer){
        observersList.add(observer);
    }
}