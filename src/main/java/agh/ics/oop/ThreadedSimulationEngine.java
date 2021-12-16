package agh.ics.oop;

import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

public class ThreadedSimulationEngine implements Runnable{
    private final AbstractWorldMap map;
    private boolean shouldRun = true;

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
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }
}