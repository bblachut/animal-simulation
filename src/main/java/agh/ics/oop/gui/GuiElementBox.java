package agh.ics.oop.gui;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    public Image getBoxElement(IMapElement element){
        if (element.getClass().equals(Grass.class)){
            Image grass = null;
            try {
                grass = new Image(new FileInputStream(".\\src\\main\\resources\\grass.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return grass;

        }
        if (element.getClass().equals(Animal.class)){
            Image image = null;
            switch (((Animal) element).getOrientation()){
                case 0 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\up.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 1 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\rightUp.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\right.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\rightDown.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\down.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\leftDown.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 6 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\left.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 7 -> {
                    try {
                        image = new Image(new FileInputStream(".\\src\\main\\resources\\leftUp.png"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + ((Animal) element).getOrientation());
            }
            return image;
        }
        throw new IllegalArgumentException("You can't pass objects of class " + element.getClass());
    }
}
