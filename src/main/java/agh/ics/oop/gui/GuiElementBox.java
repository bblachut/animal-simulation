package agh.ics.oop.gui;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import agh.ics.oop.MapDirection;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    static ImageView up;
    static ImageView down;
    static ImageView left;
    static ImageView right;
    static ImageView grass;

    static {
        try {
            Image imageUp = new Image(new FileInputStream(".\\src\\main\\resources\\up.png"));
            up = new ImageView(imageUp);
            Image imageDown = new Image(new FileInputStream(".\\src\\main\\resources\\down.png"));
            down = new ImageView(imageDown);
            Image imageLeft = new Image(new FileInputStream(".\\src\\main\\resources\\left.png"));
            left = new ImageView(imageLeft);
            Image imageRight = new Image(new FileInputStream(".\\src\\main\\resources\\right.png"));
            right = new ImageView(imageRight);
            Image imageGrass = new Image(new FileInputStream(".\\src\\main\\resources\\grass.png"));
            grass = new ImageView(imageGrass);
            grass.setFitHeight(20);
            grass.setFitWidth(20);
            up.setFitWidth(20);
            up.setFitHeight(20);
            left.setFitHeight(20);
            left.setFitHeight(20);
            right.setFitHeight(20);
            right.setFitHeight(20);
            down.setFitHeight(20);
            down.setFitHeight(20);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public GuiElementBox(){

    }
    public VBox getBoxElement(IMapElement element){
        if (element.getClass().equals(Grass.class)){
            return new VBox(grass, new Label("trawa"));
        }
        if (element.getClass().equals(Animal.class)){
            VBox virtualBox;
            switch (((Animal) element).getOrientation()){
                case NORTH -> virtualBox =  new VBox(up, new Label(element.getPosition().toString()));
                case SOUTH -> virtualBox =  new VBox(down, new Label(element.getPosition().toString()));
                case WEST -> virtualBox =  new VBox(left, new Label(element.getPosition().toString()));
                case EAST -> virtualBox =  new VBox(right, new Label(element.getPosition().toString()));
                default -> throw new IllegalStateException("Unexpected value: " + ((Animal) element).getOrientation());
            }
            return virtualBox;
        }
        throw new IllegalArgumentException("You can't pass objects of class " + element.getClass());
    }
}
