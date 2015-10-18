/*
 Chromis POS  - The New Face of Open Source POS
 Copyright (c) 2015 (John Lewis) Chromis.co.uk

 http://www.chromis.co.uk

 kitchen Screen v1.5

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
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.DataLogicKitchen;
import uk.chromis.utils.FixedStack;

/**
 * FXML Controller class
 *
 * @author John Lewis 2015
 */
public class KitchenscrController implements Initializable {

    public Button exit;
    public Button completed;
    public Button recall;
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
    public static String selectedOrderId;
    public static List<Orders> selectedOrder;
    public static Integer selectedOrderNum;
    private DataLogicKitchen dl_kitchen;
    private List<String> distinct;
    private List<Orders> orders;

    public static HashMap<Integer, Object> idLabels = new HashMap<>();
    public static HashMap<Integer, String> ticketIds = new HashMap<>();

    public static HashMap<Integer, Object> timeLabels = new HashMap<>();
    public static HashMap<Integer, Long> startTimes = new HashMap<>();

    public static HashMap<Integer, ObservableList> orderLists = new HashMap<>();

    public static HashMap<Integer, String> orderIds = new HashMap<>();

    /* N Deppe Sept 2015 - Added to keep a list of complete order data by block */
    public static HashMap<Integer, List<Orders>> orderDataList = new HashMap<>();
    public static HashMap<Integer, Orders> orderData = new HashMap<>();

    public static ObservableList ordersWaiting = FXCollections.observableArrayList();
    public static ObservableList order0list = FXCollections.observableArrayList();
    public static ObservableList order1list = FXCollections.observableArrayList();
    public static ObservableList order2list = FXCollections.observableArrayList();
    public static ObservableList order3list = FXCollections.observableArrayList();
    public static ObservableList order4list = FXCollections.observableArrayList();
    public static ObservableList order5list = FXCollections.observableArrayList();
    public static ObservableList order6list = FXCollections.observableArrayList();
    public static ObservableList order7list = FXCollections.observableArrayList();

    /* N Deppe Sept 2015 - Keeps track of all closed orders, up to number of stack entries in configuration */
    private FixedStack<List<Orders>> closedOrders;

    /* N Deppe Sept 2015 - Added to keep track of scene for keyboard interaction */
    private Scene thisScene;

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

        // N. Deppe - Keep stack of all closed orders
        // Get the stack size from configuration
        int stackSize;
        try {
            stackSize = Integer.parseInt(AppConfig.getInstance().getProperty("recall.historycount"));
        } catch (Exception e) {
            stackSize = 10;
        }
        if (stackSize >= 1) {
            closedOrders = new FixedStack<>(stackSize);
        } else {
            closedOrders = null;
        }
        // Recall button is initially not visible.  It is visible when an order is closed.
        displayRecallButton();

        new javax.swing.Timer(1000, new PrintTimeAction()).start();
        new javax.swing.Timer(5000, new updateDisplay()).start();

        order0items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(0);
        });
        order1items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(1);
        });
        order2items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(2);
        });
        order3items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(3);
        });
        order4items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(4);
        });
        order5items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(5);
        });
        order6items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(6);
        });
        order7items.setOnMouseClicked((MouseEvent event) -> {
            selectOrder(7);
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

        // Determine what to do
        String exitAction = AppConfig.getInstance().getProperty("misc.exitaction");
        if (exitAction==null){exitAction="0";};
        switch (exitAction) {
            case "0": // Exit, do not perform additional action
                System.exit(0);
                break;
            case "1": // Prompt for action or default
            case "":

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exit Kitchen");
                alert.setX(100);
                alert.setY(150);
                if ("monitor".equals(KitchenScr.parameter)) {
                    alert.setHeaderText("");
                    alert.setContentText("Do You want to exit the Kitchen screen?");
                } else {
                    alert.setHeaderText("Notice :  \nIf you close the kitchen for the day any unprocessed orders will be deleted from the database.");
                    alert.setContentText("Do You want to close the Kitchen for the Day?");
                }
                ButtonType buttonClearExit = new ButtonType("Close Kitchen");
                ButtonType buttonClearDisplayExit = new ButtonType("Close Display");
                ButtonType buttonCancel = new ButtonType("Cancel");
                ButtonType buttonExit = new ButtonType("Exit");
                if ("monitor".equals(KitchenScr.parameter)) {
                    alert.getButtonTypes().setAll(buttonExit, buttonCancel);
                } else {
                    alert.getButtonTypes().setAll(buttonExit, buttonClearExit, buttonClearDisplayExit, buttonCancel);
                }
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonClearExit) {
                    dl_kitchen.removeAllOrders();
                    System.exit(0);
                } else if (result.get() == buttonClearDisplayExit) {
                    dl_kitchen.removeAllOrdersDisplay();
                    System.exit(0);
                } else if (result.get() == buttonExit) {
                    System.exit(0);
                }

                break;
            case "2": // Automatically close orders for entire kitchen
                dl_kitchen.removeAllOrders();
                System.exit(0);
                break;
            case "3": // Automatically close ordres for this display only
                dl_kitchen.removeAllOrdersDisplay();
                System.exit(0);
                break;

        }

    }

    public void handleCompleteOrder() {
        if (!"monitor".equals(KitchenScr.parameter)) {
            if (selectedOrderId != null) {
                dl_kitchen.removeOrder(selectedOrderId);
                closedOrders.push(selectedOrder);  // add to closed order history
                orderDataList.remove(selectedOrderNum);
                clearSelectedOrder();
            }
            buildOrderPanels();
            displayRecallButton();
        }
    }

    public void clearSelectedOrder() {
        // Clear data for the selected order and clear the button
        selectedOrderId = null;
        selectedOrder = null;
        selectedOrderNum = null;
        updateButtonText("");
    }

    /* N Deppe Sept 2015 - Recall order functionality */
    public void handleRecallOrder() {
        if (!"monitor".equals(KitchenScr.parameter)) {
            List<Orders> lastOrder;
            if (!closedOrders.isEmpty()) {
                lastOrder = closedOrders.pop();
                for (Orders currOrder : lastOrder) {
                    String strTicketId = currOrder.getTicketid();
                    if (strTicketId.length() < 9 || !strTicketId.substring(0, 9).equals("RECALLED:")) {
                        currOrder.setTicketid("RECALLED: " + currOrder.getTicketid());
                    }
                    dl_kitchen.createOrder(currOrder);
                }
                clearSelectedOrder();
                buildOrderPanels();
            }
            displayRecallButton();
        }
    }

    /* N Deppe Sept 2015 - Display recall button only if there are orders to recall */
    public void displayRecallButton() {
        if ("monitor".equals(KitchenScr.parameter)) {
            recall.setVisible(false);
        } else if (closedOrders != null) {
            recall.setVisible(!closedOrders.isEmpty());
        } else {
            recall.setVisible(false);
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
        if (selectedOrderId == null) {
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
        KitchenscrController.orderDataList.clear();

        // Populate the panel up to 8 orders
        for (int j = 0; (j < 8 && j < distinct.size()); j++) {

            orders = dl_kitchen.selectByOrderId(distinct.get(j));
            KitchenscrController.orderDataList.put(j, orders);

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

    // N Deppe - Added for keyboard interaction
    // Sets reference to the scene object and sets keyboard event
    public void setScene(Scene myScene) {

        if (myScene != null) {

            // Get the key codes
            final String strKeyComboSelOrd1 = AppConfig.getInstance().getProperty("keymap.selord1");
            final String strKeyComboSelOrd2 = AppConfig.getInstance().getProperty("keymap.selord2");
            final String strKeyComboSelOrd3 = AppConfig.getInstance().getProperty("keymap.selord3");
            final String strKeyComboSelOrd4 = AppConfig.getInstance().getProperty("keymap.selord4");
            final String strKeyComboSelOrd5 = AppConfig.getInstance().getProperty("keymap.selord5");
            final String strKeyComboSelOrd6 = AppConfig.getInstance().getProperty("keymap.selord6");
            final String strKeyComboSelOrd7 = AppConfig.getInstance().getProperty("keymap.selord6");
            final String strKeyComboSelOrd8 = AppConfig.getInstance().getProperty("keymap.selord7");
            final String strKeyComboComplete = AppConfig.getInstance().getProperty("keymap.complete");
            final String strKeyComboRecall = AppConfig.getInstance().getProperty("keymap.recall");
            final String strKeyComboExit = AppConfig.getInstance().getProperty("keymap.exit");

            // Set the event handler for the scene
            myScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

                private boolean keyComboSet = false;

                private KeyCodeCombination keyComboSelOrd1;
                private KeyCodeCombination keyComboSelOrd2;
                private KeyCodeCombination keyComboSelOrd3;
                private KeyCodeCombination keyComboSelOrd4;
                private KeyCodeCombination keyComboSelOrd5;
                private KeyCodeCombination keyComboSelOrd6;
                private KeyCodeCombination keyComboSelOrd7;
                private KeyCodeCombination keyComboSelOrd8;
                private KeyCodeCombination keyComboComplete;
                private KeyCodeCombination keyComboRecall;
                private KeyCodeCombination keyComboExit;

                @Override
                public void handle(KeyEvent event) {

                    if (!keyComboSet) {

                        try {
                            keyComboSelOrd1 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord1"));
                        } catch (Exception ex) {
                            keyComboSelOrd1 = new KeyCodeCombination(KeyCode.DIGIT1);
                        }
                        System.out.println("Key pressed = " + keyComboSelOrd1);
                        try {
                            keyComboSelOrd2 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord2"));
                        } catch (Exception ex) {
                            keyComboSelOrd2 = new KeyCodeCombination(KeyCode.DIGIT2);
                        }
                        try {
                            keyComboSelOrd3 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord3"));
                        } catch (Exception ex) {
                            keyComboSelOrd3 = new KeyCodeCombination(KeyCode.DIGIT3);
                        }
                        try {
                            keyComboSelOrd4 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord4"));
                        } catch (Exception ex) {
                            keyComboSelOrd4 = new KeyCodeCombination(KeyCode.DIGIT4);
                        }
                        try {
                            keyComboSelOrd5 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord5"));
                        } catch (Exception ex) {
                            keyComboSelOrd5 = new KeyCodeCombination(KeyCode.DIGIT5);
                        }
                        try {
                            keyComboSelOrd6 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord6"));
                        } catch (Exception ex) {
                            keyComboSelOrd6 = new KeyCodeCombination(KeyCode.DIGIT6);
                        }
                        try {
                            keyComboSelOrd7 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord7"));
                        } catch (Exception ex) {
                            keyComboSelOrd7 = new KeyCodeCombination(KeyCode.DIGIT7);
                        }
                        try {
                            keyComboSelOrd8 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord8"));
                        } catch (Exception ex) {
                            keyComboSelOrd8 = new KeyCodeCombination(KeyCode.DIGIT8);
                        }
                        try {
                            keyComboComplete = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.complete"));
                        } catch (Exception ex) {
                            keyComboComplete = new KeyCodeCombination(KeyCode.ENTER);
                        }
                        try {
                            keyComboRecall = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.recall"));
                        } catch (Exception ex) {
                            keyComboRecall = new KeyCodeCombination(KeyCode.R);
                        }
                        try {
                            keyComboExit = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.exit"));
                        } catch (Exception ex) {
                            keyComboExit = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.DOWN, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.ANY);
                        }
                        keyComboSet = true;
                    }

                    // Does the event keycodecombo match anything?
                    if (keyComboSelOrd1.match(event)) {
                        selectOrder(0);
                    } else if (keyComboSelOrd2.match(event)) {
                        selectOrder(1);
                    } else if (keyComboSelOrd3.match(event)) {
                        selectOrder(2);
                    } else if (keyComboSelOrd4.match(event)) {
                        selectOrder(3);
                    } else if (keyComboSelOrd5.match(event)) {
                        selectOrder(4);
                    } else if (keyComboSelOrd6.match(event)) {
                        selectOrder(5);
                    } else if (keyComboSelOrd7.match(event)) {
                        selectOrder(6);
                    } else if (keyComboSelOrd8.match(event)) {
                        selectOrder(7);
                    } else if (keyComboComplete.match(event)) {
                        handleCompleteOrder();
                    } else if (keyComboRecall.match(event)) {
                        handleRecallOrder();
                    } else if (keyComboExit.match(event)) {
                        handleExitClick();
                    }

                }

            });

            // Finally, set the scene property
            thisScene = myScene;

        }

    }

    /* N Deppe Sept 2015 - Added for keyboard interaction functionality */
    // Used to select an order
    private void selectOrder(int orderNum) {
        if (orderDataList.containsKey(orderNum)) {
            selectedOrder = orderDataList.get(orderNum);
            selectedOrderId = orderIds.get(orderNum);
            selectedOrderNum = orderNum;        
            updateButtonText(ticketIds.get(orderNum));
        } else {
            selectedOrder = null;
            selectedOrderId = null;
            selectedOrderNum = null;
            updateButtonText("");
        }
    }

}
