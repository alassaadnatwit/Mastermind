package edu.wit.Comp1050;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MasterMind.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 380.0, 706.0));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
