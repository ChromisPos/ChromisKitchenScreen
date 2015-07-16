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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.DataLogicKitchen;

/**
 * FXML Controller class
 *
 * @author John Lewis 2015
 */
public class RaspberryPiScrController implements Initializable {

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

        if ("monitor".equals(KitchenScr.parameter)) {
            completed.setVisible(false);
        }

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
        //this section is the raspberry PI
        final Stage dialogStage = new Stage();

        dialogStage.initModality(Modality.WINDOW_MODAL);
        Label label1 = new Label("\n  Notice :  \n  If you close the kitchen for the day any unprocessed orders will be deleted from the database.\n ");
        Label label2 = new Label("\n  Do You want to close the Kitchen for the Day?\n");

        Button cancelButton = new Button("Cancel");
        Button closeKitchenButton = new Button("Close Kitchen");
        Button exitButton = new Button("Exit");
        Separator spacerBar = new Separator();
        spacerBar.setPrefWidth(600);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                System.exit(0);

            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                dialogStage.close();
            }
        });

        closeKitchenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                dl_kitchen.removeAllOrders();
                System.exit(0);
            }
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        hBox.setSpacing(10.0);
        exitButton.setPrefWidth(90);
        closeKitchenButton.setPrefWidth(90);
        cancelButton.setPrefWidth(90);

        hBox.getChildren().addAll(exitButton, closeKitchenButton, cancelButton);
        hBox.setMargin(cancelButton, new Insets(0, 20, 0, 0));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label1, spacerBar, label2, hBox);

        dialogStage.setScene(new Scene(vBox, 625, 150));
        dialogStage.setTitle("Exit Kitchen Screen");

        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.showAndWait();
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
                RaspberryPiScrController.ticketIds.put(j, order.getTicketid());
                ((Label) RaspberryPiScrController.idLabels.get(j)).setText(order.getTicketid());
                RaspberryPiScrController.startTimes.put(j, order.getOrdertime().getTime());
                RaspberryPiScrController.orderIds.put(j, order.getOrderid());
                RaspberryPiScrController.orderLists.get(j).add((order.getQty() > 1 ? order.getQty() + " x " : "") + order.getDetails());
                if (!"".equals(order.getAttributes())) {
                    RaspberryPiScrController.orderLists.get(j).add(" ~~ " + order.getAttributes());
                }
                if (order.getNotes() != null) {
                    RaspberryPiScrController.orderLists.get(j).add(" ~~ " + order.getNotes());
                }
            }
        }

        if (distinct.size() < 8) {
            for (int j = distinct.size(); j < 8; j++) {
                ((Label) RaspberryPiScrController.idLabels.get(j)).setText("");
                ((Label) RaspberryPiScrController.timeLabels.get(j)).setText("");
                RaspberryPiScrController.startTimes.put(j, (long) 0);
                RaspberryPiScrController.orderLists.get(j).clear();
            }
        }

        if (distinct.size() > 7) {
            for (int j = 8; j < distinct.size(); j++) {
                orders = dl_kitchen.selectByOrderId(distinct.get(j));
                RaspberryPiScrController.ordersWaiting.add(orders.get(0).getTicketid());
            }
        }
        updateDisplays();
    }

    // clear the list of order items being shown
    private void resetItemDisplays() {
        RaspberryPiScrController.order0list.clear();
        RaspberryPiScrController.order1list.clear();
        RaspberryPiScrController.order2list.clear();
        RaspberryPiScrController.order3list.clear();
        RaspberryPiScrController.order4list.clear();
        RaspberryPiScrController.order5list.clear();
        RaspberryPiScrController.order6list.clear();
        RaspberryPiScrController.order7list.clear();
        RaspberryPiScrController.ordersWaiting.clear();

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
