/*
 Chromis  - The future of Point Of Sale
 Copyright (c) 2015 chromis.co.uk (John Lewis)
 http://www.chromis.co.uk

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

import java.awt.event.ActionListener;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.chromis.dto.Orders;
import uk.chromis.utils.DataLogicKitchen;

/**
 *
 * @author John Lewis 2015
 */
public class KitchenScr extends Application {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("kitchenscr.fxml"));
        primaryStage.setTitle("Kitchen Orders");
        primaryStage.setX(0);
         primaryStage.setY(0);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }
}
