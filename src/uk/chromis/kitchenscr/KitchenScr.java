/*
 Chromis  - The future of Point Of Sale
 Copyright (c) 2015 chromis.co.uk (John Lewis)
 http://www.chromis.co.uk

 kitchen Screen v1.01

 This file is part of chromis & its associated programs

 chromis is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 chromis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with chromis.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.chromis.kitchenscr;

import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.chromis.forms.AppConfig;
import uk.chromis.forms.AppLocal;
import uk.chromis.hibernate.HibernateUtil;

/**
 *
 * @author John Lewis 2015
 */
public class KitchenScr extends Application {

    private int width = 1024;
    private int height = 768;
    private int scrXpos = 0;
    private int scrYpos = 0;

    public static String parameter;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            parameter = args[0];
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            HibernateUtil.getSessionFactory().openSession();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to the database.");
            ButtonType buttonOK = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonOK);
            Optional<ButtonType> result = alert.showAndWait();
            Stage secondaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/uk/chromis/configuration/database.fxml"));
            secondaryStage.setTitle("Database Configuration - v" + AppLocal.APP_VERSION);
            secondaryStage.setScene(new Scene(root, 600, 380));
            setUserAgentStylesheet(STYLESHEET_MODENA);
            secondaryStage.showAndWait();
        };

        
        
        
        
        try {
            if (AppConfig.getInstance().getProperty("screen.width") != null) {
                width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
            }
            if (AppConfig.getInstance().getProperty("screen.height") != null) {
                height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
            }
        } catch (IllegalArgumentException e) {
            width = 1024;
            height = 768;
        }
        try {
            if (AppConfig.getInstance().getProperty("screen.width") != null) {
                width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
            }
            if (AppConfig.getInstance().getProperty("screen.height") != null) {
                height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
            }
        } catch (IllegalArgumentException e) {
            width = 1024;
            height = 768;
        }

        try {
            if (AppConfig.getInstance().getProperty("screen.xpos") != null) {
                width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
            }
            if (AppConfig.getInstance().getProperty("screen.ypos") != null) {
                height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
            }
        } catch (IllegalArgumentException e) {
            scrXpos = 0;
            scrYpos = 0;
        }

        Parent root = FXMLLoader.load(getClass().getResource("kitchenscr.fxml"));
        primaryStage.setTitle("Kitchen Orders");
        primaryStage.setX(scrXpos);
        primaryStage.setY(scrYpos);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }
}
