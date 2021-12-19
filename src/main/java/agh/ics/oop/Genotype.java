package agh.ics.oop;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Genotype {
    private int[] genotype = new int[32];

    public Genotype(){
        fillRandom();
    }

    public Genotype(Animal a1, Animal a2){
        mixParents(a1, a2);
    }

    public Genotype(String str){
        for (int i = 0; i < 32; i++) {
            genotype[i] = i;
        }
    }

   private void fillRandom(){
       Random rng = new Random();
       for (int i = 0; i < 32; i++) {
           genotype[i] = rng.nextInt(8);
       }
       Arrays.sort(genotype);
   }

   private void mixParents(Animal a1, Animal a2){
       boolean strongerTakesLeft = new Random().nextBoolean();
       a1.changeCurrentEnergy(-a1.getCurrentEnergy()/4);
       a2.changeCurrentEnergy(-a2.getCurrentEnergy()/4);
       int[] a1Gen = a1.getGenotype().getArray();
       int[] a2Gen = a2.getGenotype().getArray();
       for (int i = 0; i < 32; i++) {
           if (strongerTakesLeft){
               if (i<a1.getCurrentEnergy()*32/(a2.getCurrentEnergy()+a1.getCurrentEnergy())) {
                   genotype[i] = a1Gen[i];
               }else {
                   genotype[i] = a2Gen[i];
               }
           }else {
               if (i<32-(a1.getCurrentEnergy()*32/(a2.getCurrentEnergy()+a1.getCurrentEnergy()))) {
                   genotype[i] = a2Gen[i];
               }else {
                   genotype[i] = a1Gen[i];
               }
           }
       }
       Arrays.sort(genotype);
   }

   public int[] getArray(){
        return genotype;
   }

   @Override
  public boolean equals(Object other){
       if (this == other)
           return true;
       if (!(other instanceof Genotype))
           return false;
       Genotype that = (Genotype) other;
       return  Arrays.equals(this.getArray(), that.getArray());
  }

   @Override
   public int hashCode(){
        return Objects.hash(Arrays.hashCode(genotype));
   }

    @Override
    public String toString() {
        return Arrays.toString(genotype);
    }
}
