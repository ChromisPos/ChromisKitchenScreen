/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.kitchenscr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.DataLogicKitchen;

/**
 *
 * @author John
 */
public class OrderPanel {

    public ObservableList odList = FXCollections.observableArrayList();
    private final StackPane top;
    private StackPane bottom;
    private final Pane headerPane;
    private final Label orderID;
    private final Label orderTime;
    private final ListView list;
    private DataLogicKitchen dl_kitchen;
    private List<String> distinct;

    private final int amberCSS;
    private final int redCSS;
    private long elapsed;
    private Date startTime;
    private String ordID;
    private String orderIDText;
    private List<Orders> currentOrder;

    public OrderPanel() {      
        amberCSS = Integer.parseInt(AppConfig.getInstance().getProperty("screen.amber"));
        redCSS = Integer.parseInt(AppConfig.getInstance().getProperty("screen.red"));
        top = new StackPane();
        bottom = new StackPane();
        headerPane = new Pane();
        orderID = new Label();
        orderTime = new Label();
        list = new ListView();
        bottom = new StackPane();

    }

    public ListView createPanel(SplitPane splitPane) {
// Build the top panel
        top.setPrefSize(215, 33);
        top.setLayoutX(0);
        top.setLayoutY(0);

        headerPane.setPrefSize(215, 33);
        headerPane.setLayoutX(0);
        headerPane.setLayoutY(2);

        orderID.setPrefSize(165, 17);
        orderID.setLayoutX(5);
        orderID.setLayoutY(6);

        orderTime.setPrefSize(60, 17);
        orderTime.setLayoutX(170);
        orderTime.setLayoutY(6);

// Build the bottom panel 
        bottom.setPrefSize(215, 320);
        bottom.setLayoutX(0);
        bottom.setLayoutY(0);

        list.setPrefSize(216, 320);
        list.setStyle("-fx-fixed-cell-size: 24;");
        list.setLayoutX(0);
        list.setLayoutY(65);
        bottom.getChildren().add(list);

//Set the fonts        
        orderID.setFont(new Font("Tahoma Bold", 14));
        orderTime.setFont(new Font("Tahoma Bold", 14));

        headerPane.getChildren().add(orderID);
        headerPane.getChildren().add(orderTime);
        top.getChildren().add(headerPane);

        splitPane.getItems().addAll(top, bottom);
        splitPane.setDividerPositions(0.1);

        return list;
    }

    public void updateTimers(List<Orders> orders) {
        startTime = orders.get(0).getOrdertime();
        elapsed = (System.currentTimeMillis() - startTime.getTime());
        orderTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(elapsed) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed)),
                TimeUnit.MILLISECONDS.toSeconds(elapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed))));

    }

    public void clearPanel() {
        orderTime.setText("");
        orderID.setText("");
        list.getItems().clear();
        top.getStyleClass().clear();
        odList.clear();
        top.setVisible(false);
        bottom.setVisible(false);
    }

    public void removeCSS() {
        headerPane.getStyleClass().clear();
        headerPane.getStyleClass().add("status-green");
        orderID.getStyleClass().add("status-text-green");
        orderTime.getStyleClass().add("status-text-green");
    }

    public void showOrder(List<Orders> orders) {
        currentOrder = orders;
        startTime = orders.get(0).getOrdertime();
        elapsed = (System.currentTimeMillis() - startTime.getTime());
        orderTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(elapsed) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed)),
                TimeUnit.MILLISECONDS.toSeconds(elapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed))));
        orderID.setText(orders.get(0).getTicketid());
        orderIDText = orders.get(0).getTicketid();
        ordID = orders.get(0).getOrderid();

        odList.clear();
        Integer kitchenTime = 0;
        HashMap<Integer, ObservableList> orderLists = new HashMap<>();
        for (Orders order : orders) {
            odList.add((order.getQty() > 1 ? order.getQty() + " x " : "") + order.getDetails());
            if (!"".equals(order.getAttributes())) {
                odList.add(" ~~ " + order.getAttributes());
            }
            if (order.getNotes() != null) {
                odList.add(" -- " + order.getNotes());
            }
            
            //lets check if we have procudt the needs specific prep time in the order
            if (order.getKitchenTime() != null && (order.getKitchenTime() > kitchenTime)){
                kitchenTime = order.getKitchenTime();
            }
        }

        list.setItems(odList);
        
        Integer redAlert = (redCSS > kitchenTime)? redCSS : kitchenTime;   
        if (TimeUnit.MILLISECONDS.toMinutes(elapsed) > amberCSS && TimeUnit.MILLISECONDS.toMinutes(elapsed) <= redAlert) {       
            top.getStyleClass().clear();
            top.getStyleClass().add("status-amber");
        } else if (TimeUnit.MILLISECONDS.toMinutes(elapsed) > redAlert) {
            top.getStyleClass().clear();
            top.getStyleClass().add("status-red");
        } else {
            top.getStyleClass().clear();
            top.getStyleClass().add("status-green");
        }

        top.setVisible(true);
        bottom.setVisible(true);
    }

    public List<Orders> getCurrentOrder() {
        return currentOrder;
    }

    public String getOrder() {
        return orderIDText;
    }

    public String getOrderID() {
        return ordID;
    }

}
