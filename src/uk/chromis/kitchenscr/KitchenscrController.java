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

import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.DataLogicKitchen;

/**
 * FXML Controller class
 *
 * @author John Lewis 2015
 */
public class KitchenscrController implements Initializable {

    public Button exit;
    public Button completed;
    public Label clock;

    public Label order0id;
    public Label order0time;
    public Label order1id;
    public Label order1time;
    public Label order2id;
    public Label order2time;
    public Label order3id;
    public Label order3time;
    public Label order4id;
    public Label order4time;
    public Label order5id;
    public Label order5time;
    public Label order6id;
    public Label order6time;
    public Label order7id;
    public Label order7time;

    public ListView order0items;
    public ListView order1items;
    public ListView order2items;
    public ListView order3items;
    public ListView order4items;
    public ListView order5items;
    public ListView order6items;
    public ListView order7items;

    public ListView orderlist;


    private Label tmpLabel;
    // private long startTime;
    private DateFormat dateFormat;
    private String hms;
    public static String selectedOrder;
    private DataLogicKitchen dl_kitchen;
    private List<String> distinct;
    private List<Orders> orders;

    public static HashMap<Integer, Object> idLabels = new HashMap<>();
    public static HashMap<Integer, String> ticketIds = new HashMap<>();

    public static HashMap<Integer, Object> timeLabels = new HashMap<>();
    public static HashMap<Integer, Long> startTimes = new HashMap<>();

    public static HashMap<Integer, ObservableList> orderLists = new HashMap<>();

    public static HashMap<Integer, String> orderIds = new HashMap<>();

    public static ObservableList ordersWaiting = FXCollections.observableArrayList();
    public static ObservableList order0list = FXCollections.observableArrayList();
    public static ObservableList order1list = FXCollections.observableArrayList();
    public static ObservableList order2list = FXCollections.observableArrayList();
    public static ObservableList order3list = FXCollections.observableArrayList();
    public static ObservableList order4list = FXCollections.observableArrayList();
    public static ObservableList order5list = FXCollections.observableArrayList();
    public static ObservableList order6list = FXCollections.observableArrayList();
    public static ObservableList order7list = FXCollections.observableArrayList();

    private class PrintTimeAction implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateClock();
                    updateTimers();

                }
            });
        }
    }

    private class updateDisplay implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    buildOrderPanels();
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dl_kitchen = new DataLogicKitchen();

        new javax.swing.Timer(1000, new PrintTimeAction()).start();
        new javax.swing.Timer(5000, new updateDisplay()).start();

        order0items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(0);
            updateButtonText(ticketIds.get(0));
        });
        order1items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(1);
            updateButtonText(ticketIds.get(1));
        });
        order2items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(2);
            updateButtonText(ticketIds.get(2));
        });
        order3items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(3);
            updateButtonText(ticketIds.get(3));
        });
        order4items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(4);
            updateButtonText(ticketIds.get(4));
        });
        order5items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(5);
            updateButtonText(ticketIds.get(5));
        });
        order6items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(6);
            updateButtonText(ticketIds.get(6));
        });
        order7items.setOnMouseClicked((MouseEvent event) -> {
            selectedOrder = orderIds.get(7);
            updateButtonText(ticketIds.get(7));
        });

        try {
            if (AppConfig.getInstance().getProperty("clock.time") != null) {
                dateFormat = new SimpleDateFormat(AppConfig.getInstance().getProperty("clock.time"));
            } else {
                dateFormat = new SimpleDateFormat("HH:mm:ss");
            }
        } catch (IllegalArgumentException e) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }

        createMaps();
        buildOrderPanels();
    }

    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exit Kitchen");
        alert.setX(100);
        alert.setY(150);
        alert.setHeaderText("Notice :  \nIf you close the kitchen for day any unprocessed orders will be deleted from the database.");
        alert.setContentText("Do You want to close the Kitchen for the Day?");
        ButtonType buttonSaveExit = new ButtonType("Close Kitchen");
        ButtonType buttonCancel = new ButtonType("Cancel");
        ButtonType buttonExit = new ButtonType("Exit");
        alert.getButtonTypes().setAll(buttonExit, buttonSaveExit, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonSaveExit) {
            dl_kitchen.removeAllOrders();
            System.exit(0);
        } else if (result.get() == buttonExit) {
            System.exit(0);
        }
    }

    public void handleCompleteOrder() {
        if (selectedOrder != null) {;
            dl_kitchen.removeOrder(selectedOrder);
            selectedOrder = null;
            updateButtonText("");
        }
        buildOrderPanels();
    }

    private void updateClock() {
        clock.setText(dateFormat.format(new Date()));
    }

    private String getTime(long milliseconds) {
        hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return (hms);
    }

    private void createMaps() {
        idLabels.put(0, order0id);
        idLabels.put(1, order1id);
        idLabels.put(2, order2id);
        idLabels.put(3, order3id);
        idLabels.put(4, order4id);
        idLabels.put(5, order5id);
        idLabels.put(6, order6id);
        idLabels.put(7, order7id);

        timeLabels.put(0, order0time);
        timeLabels.put(1, order1time);
        timeLabels.put(2, order2time);
        timeLabels.put(3, order3time);
        timeLabels.put(4, order4time);
        timeLabels.put(5, order5time);
        timeLabels.put(6, order6time);
        timeLabels.put(7, order7time);

        orderLists.put(0, order0list);
        orderLists.put(1, order1list);
        orderLists.put(2, order2list);
        orderLists.put(3, order3list);
        orderLists.put(4, order4list);
        orderLists.put(5, order5list);
        orderLists.put(6, order6list);
        orderLists.put(7, order7list);
    }

    private void updateTimers() {
        for (int j = 0; j < 8; j++) {
            if (startTimes.get(j) > 0) {
                long elapsed = (System.currentTimeMillis() - startTimes.get(j));
                tmpLabel = (Label) timeLabels.get(j);
                tmpLabel.setText(getTime(elapsed));
            }
        }
    }

    private void updateButtonText(String id) {
        if (selectedOrder == null) {
            completed.setText("");
        } else {
            completed.setText("Order :  '" + id + "'  Complete.");
        }
    }

    private void updateLabels() {
        for (int j = 0; j < 8; j++) {
            tmpLabel = (Label) idLabels.get(j);
            tmpLabel.setText(ticketIds.get(j));
        }
    }

    private void buildOrderPanels() {
        resetItemDisplays();

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
        updateDisplays();
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

    private void updateDisplays() {
        order0items.setItems(order0list);
        order1items.setItems(order1list);
        order2items.setItems(order2list);
        order3items.setItems(order3list);
        order4items.setItems(order4list);
        order5items.setItems(order5list);
        order6items.setItems(order6list);
        order7items.setItems(order7list);
        orderlist.setItems(ordersWaiting);
    }
}
