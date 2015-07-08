/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.createtable;

import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.chromis.forms.AppLocal;

/**
 *
 * @author John
 */
public class CreateTable extends Application {
     public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("createtable.fxml"));
        primaryStage.setTitle("Create Database Table" + AppLocal.APP_VERSION);
        primaryStage.setScene(new Scene(root, 520, 150));
        setUserAgentStylesheet(STYLESHEET_MODENA);

        primaryStage.show();

    }
    
    
    
}
