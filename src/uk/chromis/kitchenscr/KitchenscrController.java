/*
 Chromis POS  - The New Face of Open Source POS
 Copyright (c) 2018 (John Lewis) Chromis.co.uk

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.DataLogicKitchen;
import uk.chromis.utils.FixedStack;

/**
 * FXML Controller class
 *
 * @author John Lewis 2018
 */
public class KitchenscrController implements Initializable {

    public Button exit;
    public Button completed;
    public Button recall;
    public Label clock;

    public GridPane gridPane;
    public SplitPane orderPane0;
    public SplitPane orderPane1;
    public SplitPane orderPane2;
    public SplitPane orderPane3;
    public SplitPane orderPane4;
    public SplitPane orderPane5;
    public SplitPane orderPane6;
    public SplitPane orderPane7;
    public ListView waitingOrderList;
    public AnchorPane mainscr;

    private DateFormat dateFormat;
    private String selectedOrderId;
    private List<Orders> selectedOrder;
    private Integer selectedOrderNum;
    private DataLogicKitchen dl_kitchen;
    private List<String> distinct;
    private List<Orders> orders;

    private OrderPanel tmpOrder;

    private EventHandler evHandler;
    private KeyCodeCombination tmpKeys;

    private final ObservableList ordersWaiting = FXCollections.observableArrayList();
    private final HashMap<Integer, Object> orderPanes = new HashMap<>();
    private FixedStack<List<Orders>> closedOrders;

    private class PrintTimeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    clock.setText(dateFormat.format(new Date()));
                    for (int j = 0; (j < 8 && j < distinct.size()); j++) {
                        tmpOrder = (OrderPanel) orderPanes.get(j);
                        orders = dl_kitchen.selectByOrderId(distinct.get(j));
                        tmpOrder.updateTimers(orders);
                    }
                }
            });
        }
    }

    private class updateDisplay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
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
        try {
            if (AppConfig.getInstance().getProperty("clock.time") != null) {
                dateFormat = new SimpleDateFormat(AppConfig.getInstance().getProperty("clock.time"));
            } else {
                dateFormat = new SimpleDateFormat("HH:mm:ss");
            }
        } catch (IllegalArgumentException e) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }

        if ("monitor".equals(KitchenScr.parameter)) {
            completed.setVisible(false);
        }

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
        displayRecallButton();

        dl_kitchen = new DataLogicKitchen();

        //create & build display array
        for (int j = 0; j <= 7; j++) {
            OrderPanel ods = new OrderPanel();
            orderPanes.put(j, ods);
            int i = j;
            ListView list = ods.createPanel((SplitPane) gridPane.getChildren().get(j));

            list.setOnMouseClicked((MouseEvent event) -> {
                selectOrder(i);
            });

            try {
                tmpKeys = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord" + (j + 1)));
            } catch (Exception ex) {
                tmpKeys = new KeyCodeCombination(KeyCode.DIGIT1);
            }
            createEvent(tmpKeys, i, null);
        }

        try {
            tmpKeys = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.complete"));
        } catch (Exception ex) {
            tmpKeys = new KeyCodeCombination(KeyCode.ENTER);
        }
        createEvent(tmpKeys, 0, "complete");

        try {
            tmpKeys = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.recall"));
        } catch (Exception ex) {
            tmpKeys = new KeyCodeCombination(KeyCode.R);
        }
        createEvent(tmpKeys, 0, "recall");

        try {
            tmpKeys = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.exit"));
        } catch (Exception ex) {
            tmpKeys = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.DOWN, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.ANY);
        }
        createEvent(tmpKeys, 0, "exit");

        new javax.swing.Timer(1000, new PrintTimeAction()).start();
        new javax.swing.Timer(5000, new updateDisplay()).start();

        buildOrderPanels();
    }

    private void createEvent(KeyCodeCombination tmpKeys, int i, String action) {
        evHandler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (tmpKeys.match(ke)) {
                    if (action == null) {
                        selectOrder(i);
                    } else {
                        switch (action.toLowerCase()) {
                            case "exit":
                                handleExitClick();
                                break;
                            case "recall":
                                handleRecallOrder();
                                break;
                            case "complete":
                                handleCompleteOrder();
                                break;
                        }
                    }
                }
            }
        };
        mainscr.addEventFilter(KeyEvent.KEY_PRESSED, evHandler);
    }

    private void buildOrderPanels() {
        distinct = dl_kitchen.readDistinctOrders();
        ordersWaiting.clear();

        for (int j = 0; (j < 8 && j < distinct.size()); j++) {
            tmpOrder = (OrderPanel) orderPanes.get(j);
            orders = dl_kitchen.selectByOrderId(distinct.get(j));
            tmpOrder.showOrder(orders);
        }

        if (distinct.size() == 0) {
            tmpOrder = (OrderPanel) orderPanes.get(0);
            tmpOrder.clearPanel();
        } else {
            for (int j = distinct.size(); j < 8; j++) {
                tmpOrder = (OrderPanel) orderPanes.get(j);
                tmpOrder.clearPanel();
            }
        }

        if (distinct.size() > 7) {
            for (int j = 8; j < distinct.size(); j++) {
                orders = dl_kitchen.selectByOrderId(distinct.get(j));
                ordersWaiting.add(orders.get(0).getTicketid());
            }
        }
        waitingOrderList.setItems(ordersWaiting);
    }

    private void selectOrder(int orderNum) {
        tmpOrder = (OrderPanel) orderPanes.get(orderNum);
        selectedOrderId = tmpOrder.getOrderID();
        selectedOrder = tmpOrder.getCurrentOrder();
        updateButtonText(selectedOrderId, tmpOrder.getOrder());

    }

    public void handleCompleteOrder() {
        if (!"monitor".equals(KitchenScr.parameter)) {
            if (selectedOrderId != null) {
                dl_kitchen.removeOrder(selectedOrderId);
                closedOrders.push(selectedOrder);  // add to closed order history
                updateButtonText(null, "");
                buildOrderPanels();
                displayRecallButton();
            }
        }
    }

    private void updateButtonText(String orderid, String orderIdText) {
        if (orderid == null) {
            completed.setText("");
        } else {
            completed.setText("Order :  '" + orderIdText + "'  Complete.");
        }
    }

    public void handleExitClick() {
        // Determine what to do
        String exitAction = AppConfig.getInstance().getProperty("misc.exitaction");
        if (exitAction == null) {
            exitAction = "0";
        };
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

    public void clearSelectedOrder() {
        selectedOrderId = null;
        selectedOrder = null;
        updateButtonText(selectedOrderId, "");
    }

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

    public void displayRecallButton() {
        if ("monitor".equals(KitchenScr.parameter)) {
            recall.setVisible(false);
        } else if (closedOrders != null) {
            recall.setVisible(!closedOrders.isEmpty());
        } else {
            recall.setVisible(false);
        }
    }

}
