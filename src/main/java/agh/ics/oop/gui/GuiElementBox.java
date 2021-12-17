package agh.ics.oop.gui;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private  Image[] images;

    {
        try {
            images = new Image[]{new Image(new FileInputStream(".\\src\\main\\resources\\grass.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\up.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\rightUp.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\right.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\rightDown.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\down.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\leftDown.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\left.png")),
                    new Image(new FileInputStream(".\\src\\main\\resources\\leftUp.png")),
            };
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Image getBoxElement(IMapElement element){

        if (element.getClass().equals(Grass.class)){
            return images[0];

        }
        if (element.getClass().equals(Animal.class)){
            switch (((Animal) element).getOrientation()){
                case 0 -> {
                    return images[1];
                }
                case 1 -> {
                    return images[2];
                }
                case 2 -> {
                    return images[3];
                }
                case 3 -> {
                    return images[4];
                }
                case 4 -> {
                    return images[5];
                }
                case 5 -> {
                    return images[6];
                }
                case 6 -> {
                    return images[7];
                }
                case 7 -> {
                    return images[8];
                }
                default -> throw new IllegalStateException("Unexpected value: " + ((Animal) element).getOrientation());
            }
        }
        throw new IllegalArgumentException("You can't pass objects of class " + element.getClass());
    }
}
