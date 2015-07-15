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
package uk.chromis.configuration;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import uk.chromis.forms.AppConfig;
import uk.chromis.hibernate.HibernateUtil;
import uk.chromis.utils.AltEncrypter;
import uk.chromis.utils.DirtyManager;
import javafx.geometry.Pos;

/**
 * FXML Controller class
 *
 */
public class DatabaseController implements Initializable {

    public ComboBox<String> jcboDBDriver;

    public TextField jtxtDbDriver;
    public TextField jtxtDbURL;
    public TextField jtxtDbUser;
    public TextField jtxtDbPassword;
    public TextField jtxtDialect;
    public TextField jtxtWidth;
    public TextField jtxtHeight;
    public Button save;
    public Spinner displayNumber;
    public TextField jtxtClockFormat;

    private final DirtyManager dirty = new DirtyManager();
    private String display;
    private AltEncrypter cypher;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        jcboDBDriver.valueProperty().addListener(dirty);
        displayNumber.valueProperty().addListener(dirty);
        jtxtDbDriver.textProperty().addListener(dirty);
        jtxtDbURL.textProperty().addListener(dirty);
        jtxtDbUser.textProperty().addListener(dirty);
        jtxtDbPassword.textProperty().addListener(dirty);
        jtxtWidth.textProperty().addListener(dirty);
        jtxtHeight.textProperty().addListener(dirty);
        jtxtClockFormat.textProperty().addListener(dirty);

        jcboDBDriver.setOnAction(e -> {
            if ("Apache Derby Client/Server".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("org.apache.derby.jdbc.ClientDriver");
                jtxtDbURL.setText("jdbc:derby://localhost:1527/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.DerbyDialect");
            } else if ("MySQL".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("com.mysql.jdbc.Driver");
                jtxtDbURL.setText("jdbc:mysql://localhost:3306/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.MySQLDialect");
            } else if ("Oracle 11g Express".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("oracle.jdbc.driver.OracleDriver");
                jtxtDbURL.setText("jdbc:oracle:thin://localhost:1521/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.OracleDialect");
            } else if ("PostgreSQL".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("org.postgresql.Driver");
                jtxtDbURL.setText("jdbc:postgresql://localhost:5432/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.PostgreSQLDialect");
            } else {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("");
                jtxtDbURL.setText("");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("");
            }
        });
        jtxtWidth.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!checkNumber(newValue)) {
                    jtxtWidth.setText(oldValue);
                }
            }
        });

        jtxtHeight.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!checkNumber(newValue)) {
                    jtxtHeight.setText(oldValue);
                }
            }
        });

        loadProperties();

    }

    public void loadProperties() {
        jcboDBDriver.setValue(AppConfig.getInstance().getProperty("db.engine"));
        jtxtDbDriver.setText(AppConfig.getInstance().getProperty("db.driver"));
        jtxtDbURL.setText(AppConfig.getInstance().getProperty("db.URL"));
        jtxtDialect.setText(AppConfig.getInstance().getProperty("db.dialect"));
        jtxtWidth.setText(AppConfig.getInstance().getProperty("screen.width"));
        jtxtHeight.setText(AppConfig.getInstance().getProperty("screen.height"));
        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        jtxtDbUser.setText(sDBUser);
        jtxtDbPassword.setText(sDBPassword);

        display = (AppConfig.getInstance().getProperty("screen.displaynumber"));
        if (display == null || "".equals(display)) {
            displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
        } else {
            displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, Integer.parseInt(display)));
        }

        if (jtxtWidth.getText() == null || "".equals(jtxtWidth.getText())) {
            jtxtWidth.setText("1024");
            jtxtHeight.setText("768");
        }

        jtxtClockFormat.setText(AppConfig.getInstance().getProperty("clock.time"));

        dirty.resetDirty();
    }

    public void handleSaveClick() throws IOException, LiquibaseException {
        AppConfig.getInstance().setProperty("db.engine", jcboDBDriver.getValue());
        AppConfig.getInstance().setProperty("screen.displaynumber", displayNumber.getValue().toString());
        AppConfig.getInstance().setProperty("db.driver", jtxtDbDriver.getText());
        AppConfig.getInstance().setProperty("db.URL", jtxtDbURL.getText());
        AppConfig.getInstance().setProperty("db.user", jtxtDbUser.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtDbUser.getText());
        AppConfig.getInstance().setProperty("db.password", "crypt:" + cypher.encrypt(new String(jtxtDbPassword.getText())));
        AppConfig.getInstance().setProperty("db.dialect", jtxtDialect.getText());
        if (Integer.parseInt(jtxtHeight.getText()) > screenSize.height) {
            jtxtHeight.setText(String.valueOf(screenSize.height));
        }

        if (Integer.parseInt(jtxtWidth.getText()) > screenSize.width) {
            jtxtWidth.setText(String.valueOf(screenSize.width));
        }

        AppConfig.getInstance().setProperty("screen.width", jtxtWidth.getText());
        AppConfig.getInstance().setProperty("screen.height", jtxtHeight.getText());
        AppConfig.getInstance().setProperty("clock.time", jtxtClockFormat.getText());

        AppConfig.getInstance().save();
        dirty.resetDirty();

        Boolean error = false;
        try {
            HibernateUtil.getSessionFactory().openSession();
        } catch (Exception ex) {
            error = true;
        }
        if (error == false) {
            String sDBUser = AppConfig.getInstance().getProperty("db.user");
            String sDBPassword = AppConfig.getInstance().getProperty("db.password");
            if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
                cypher = new AltEncrypter("cypherkey" + sDBUser);
                sDBPassword = cypher.decrypt(sDBPassword.substring(6));
            }
            String url = AppConfig.getInstance().getProperty("db.URL");
            Session session = HibernateUtil.getSessionFactory().openSession();
            SessionImpl sessionImpl = (SessionImpl) session;

            Connection connection = sessionImpl.connection();
            try {
                String changelog = "uk/chromis/createtable/kitchentable.xml";
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
                liquibase.update("implement");
            } catch (DatabaseException e) {
            }
        }

    }

    public void handleExitClick() throws IOException, LiquibaseException {

       
         //this section is the raspberry PI
         final Stage dialogStage = new Stage();

         dialogStage.initModality(Modality.WINDOW_MODAL);
         Label label = new Label("You have changed data, that has not been changed. What do you wish to do? ");

         Button saveAndExitButton = new Button("Save & Exit");
         Button exitButton = new Button("Exit");

         exitButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent arg0) {
         System.exit(0);

         }
         });

         saveAndExitButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent arg0) {
             try {
                 handleSaveClick();
             } catch (IOException ex) {
                 Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (LiquibaseException ex) {
                 Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
             }
         System.exit(0);
         }
         });



         HBox hBox = new HBox();
         hBox.setAlignment(Pos.BASELINE_RIGHT);
         hBox.setSpacing(10.0);
         exitButton.setPrefWidth(90);
         saveAndExitButton.setPrefWidth(90);
         

         hBox.getChildren().addAll(saveAndExitButton, exitButton);
         hBox.setMargin(exitButton, new Insets(0, 20, 0, 0));
         VBox vBox = new VBox();
         vBox.getChildren().addAll(label, hBox);

         dialogStage.setScene(new Scene(vBox, 325, 150, Color.CORAL));

         dialogStage.initModality(Modality.APPLICATION_MODAL);
         dialogStage.initStyle(StageStyle.UNDECORATED);
         dialogStage.setX(100);
         dialogStage.setY(150);
         dialogStage.show();                
        
        // all other version 
        if (dirty.isDirty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Exit Configuration");
            alert.setHeaderText(null);
            alert.setContentText("You have changed data, that has not been changed. What do you wish to do?");
            ButtonType buttonSaveExit = new ButtonType("Save & Exit");
            ButtonType buttonExit = new ButtonType("Exit");
            alert.getButtonTypes().setAll(buttonSaveExit, buttonExit);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonSaveExit) {
                handleSaveClick();
                System.exit(0);
            } else {
                System.exit(0);
            }
        }
        System.exit(0);

    }

    public Boolean checkNumber(String number) {
        if (number == null) {
            return true;
        }
        String regex = "[0-9]+";
        return number.matches(regex);

    }
}
