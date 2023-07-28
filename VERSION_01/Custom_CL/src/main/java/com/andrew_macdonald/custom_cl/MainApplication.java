/*
Project: Custom Cover Letter Creator
Created By: Andrew Macdonald
Created On: 2023-07-15

File Name : MainApplication.java
Revision History:
         Andrew Macdonald, 2023-07-15 : Created
*/


package com.andrew_macdonald.custom_cl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        // Customize the stage
        stage.setTitle("Custom Cover Letter Creator");
        stage.setScene(scene);
        // stage.setFullScreen(true); // make the stage full screen
        // Change the icon
        InputStream iconStream = new FileInputStream("D:/cover_letter_maker/VERSION_01/Custom_CL/images/donatello3.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}