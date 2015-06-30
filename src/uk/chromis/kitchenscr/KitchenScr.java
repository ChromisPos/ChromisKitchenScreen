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

    private final DataLogicKitchen dl_kitchen = new DataLogicKitchen();
    private List<String> distinct;
    private List<Orders> orders;
    private final KitchenscrController display = new KitchenscrController();

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

   //    resetValues();
//        resetItemDisplays();
//        buildOrderPanels();

        // create the timer task to read the database        
       // new javax.swing.Timer(5000, new readOrders()).start();

    }

    private class readOrders implements ActionListener {
        // Update display fields 

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    resetItemDisplays();
                    buildOrderPanels();
                }
            }
            );
        }

    }

    private void buildOrderPanels() {
        // Get list of unique orders
        distinct = dl_kitchen.readDistinctOrders();

        // Populate the panel up to 8 orders
        for (int j = 0; (j < 8 && j < distinct.size()); j++) {

            orders = dl_kitchen.selectByOrderId(distinct.get(j));

            for (Orders order : orders) {
                KitchenscrController.ticketIds.put(j, order.getTicketid());
                ((Label) KitchenscrController.idLabels.get(j)).setText(order.getTicketid());
                KitchenscrController.startTimes.put(j, order.getOrdertime().getTime());
                KitchenscrController.orderIds.put(j, order.getOrderid());
                KitchenscrController.orderLists.get(j).add((order.getQty() > 1 ? order.getQty() + " x " : "") + order.getDetails());
                if (!"".equals(order.getAttributes())) {
                    KitchenscrController.orderLists.get(j).add(" ~~ " + order.getAttributes());
                }
                if (order.getNotes() != null) {
                    KitchenscrController.orderLists.get(j).add(" ~~ " + order.getNotes());
                }
            }
        }

        if (distinct.size() < 8) {
            for (int j = distinct.size(); j < 8; j++) {
                ((Label) KitchenscrController.idLabels.get(j)).setText("");
                ((Label) KitchenscrController.timeLabels.get(j)).setText("");
                KitchenscrController.startTimes.put(j, (long) 0);
                KitchenscrController.orderLists.get(j).clear();
            }
        }

        if (distinct.size() > 7) {
            for (int j = 8; j < distinct.size(); j++) {
                orders = dl_kitchen.selectByOrderId(distinct.get(j));
                KitchenscrController.ordersWaiting.add(orders.get(0).getTicketid());
            }
        }

    }

    // clear the list of order items being shown
    private void resetItemDisplays() {
        KitchenscrController.order0list.clear();
        KitchenscrController.order1list.clear();
        KitchenscrController.order2list.clear();
        KitchenscrController.order3list.clear();
        KitchenscrController.order4list.clear();
        KitchenscrController.order5list.clear();
        KitchenscrController.order6list.clear();
        KitchenscrController.order7list.clear();
        KitchenscrController.ordersWaiting.clear();

    }

    private void resetValues() {
        for (int j = 0; j < 8; j++) {
            KitchenscrController.ticketIds.put(j, "");
            KitchenscrController.startTimes.put(j, (long) 0);
        }
    }

}
