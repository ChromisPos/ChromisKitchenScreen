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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.chromis.forms.AppConfig;

/**
 *
 * @author John Lewis 2015
 */
public class KitchenScr extends Application {

    private int width = 1024;
    private int height = 768;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
        if (AppConfig.getInstance().getProperty("screen.width") != null) {
            width = Integer.parseInt(AppConfig.getInstance().getProperty("screen.width"));
        }
        if(AppConfig.getInstance().getProperty("screen.height") != null)
        {
            height = Integer.parseInt(AppConfig.getInstance().getProperty("screen.height"));
        }
        } catch (IllegalArgumentException e){
            width = 1024;
            height = 768;
        }

        Parent root = FXMLLoader.load(getClass().getResource("kitchenscr.fxml"));
        primaryStage.setTitle("Kitchen Orders");
        primaryStage.setX(50);
        primaryStage.setY(50);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }
}
