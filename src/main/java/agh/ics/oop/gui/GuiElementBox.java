package agh.ics.oop.gui;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.interfaces.IMapElement;
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
            int orientation = ((Animal) element).getOrientation();
            if (orientation>=0 && orientation<8){
                return images[orientation+1];
            }else {
                throw new IllegalStateException("Unexpected value: " + orientation);
            }
        }
        throw new IllegalArgumentException("You can't pass objects of class " + element.getClass());
    }
}
