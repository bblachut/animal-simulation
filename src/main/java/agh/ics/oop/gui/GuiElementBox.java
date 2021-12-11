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
    public VBox getBoxElement(IMapElement element){
        if (element.getClass().equals(Grass.class)){
            ImageView grass = null;
            try {
                grass = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\grass.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            grass.setFitHeight(20);
            grass.setFitWidth(20);
            return new VBox(grass, new Label("trawa"));

        }
        if (element.getClass().equals(Animal.class)){
            VBox verticalBox;
            switch (((Animal) element).getOrientation()){
                case NORTH -> {
                    ImageView up = null;
                    try {
                        up = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\up.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    up.setFitHeight(20);
                    up.setFitWidth(20);
                    verticalBox = new VBox(up, new Label(element.getPosition().toString()));
                }
                case SOUTH -> {
                    ImageView down = null;
                    try {
                        down = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\down.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    down.setFitHeight(20);
                    down.setFitWidth(20);
                    verticalBox = new VBox(down, new Label(element.getPosition().toString()));
                }
                case WEST -> {
                    ImageView left = null;
                    try {
                        left = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\left.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    left.setFitHeight(20);
                    left.setFitWidth(20);
                    verticalBox = new VBox(left, new Label(element.getPosition().toString()));
                }
                case EAST -> {
                    ImageView right = null;
                    try {
                        right = new ImageView(new Image(new FileInputStream(".\\src\\main\\resources\\right.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    right.setFitHeight(20);
                    right.setFitWidth(20);
                    verticalBox = new VBox(right, new Label(element.getPosition().toString()));
                }
                default -> throw new IllegalStateException("Unexpected value: " + ((Animal) element).getOrientation());
            }
            return verticalBox;
        }
        throw new IllegalArgumentException("You can't pass objects of class " + element.getClass());
    }
}
