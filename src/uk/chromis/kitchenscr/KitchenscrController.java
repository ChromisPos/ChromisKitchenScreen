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
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private long startTime;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String hms;
    public static String selectedOrder;
    private DataLogicKitchen dl_kitchen;

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
                    //updateButtonText();
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
                    updateDisplays();
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dl_kitchen = new DataLogicKitchen();

        new javax.swing.Timer(1000, new PrintTimeAction()).start();
        new javax.swing.Timer(6000, new updateDisplay()).start();

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
        createMaps();
        resetDisplayLabels();
        updateDisplays();

    }

    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exit Kitchen");
        alert.setHeaderText("Notice :  \nIf you close the kitchen for day any unprocessed orders will be deleted from the database.");
        alert.setContentText("Do You want to close the Kitchen for the Day?");
        ButtonType buttonSaveExit = new ButtonType("Close Kitchen");
        ButtonType buttonExit = new ButtonType("Exit");
        alert.getButtonTypes().setAll(buttonSaveExit, buttonExit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonSaveExit) {
            dl_kitchen.removeAllOrders();
            System.exit(0);
        } else {
            System.exit(0);
        }

    }

    public void handleCompleteOrder() {
        if (selectedOrder != null) {;
            dl_kitchen.removeOrder(selectedOrder);
            selectedOrder = null;
            updateButtonText("");
        }
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

    private void resetDisplayLabels() {
        idLabels.entrySet().stream().map((entry) -> {
            tmpLabel = (Label) entry.getValue();
            return entry;
        }).forEach((_item) -> {
            tmpLabel.setText("");
        });

        timeLabels.entrySet().stream().map((entry) -> {
            tmpLabel = (Label) entry.getValue();
            return entry;
        }).forEach((_item) -> {
            tmpLabel.setText("");
        });
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

}
