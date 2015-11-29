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

package uk.chromis.configuration;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
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
import uk.chromis.customcontrol.*;

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
    public Spinner historyCount;
    public CheckBox jSecondscr;
    
    private final DirtyManager dirty = new DirtyManager();
    private String display;
    private AltEncrypter cypher;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private String strHistoryCount;
	 
    @FXML
    private ChoiceBox jchcExitAction;
    private Integer selectedExitActionIndex = null;
    
    @FXML
    private KeyComboTextField jtxtMapSelOrd1;
    @FXML
    private KeyComboTextField jtxtMapSelOrd2;
    @FXML
    private KeyComboTextField jtxtMapSelOrd3;
    @FXML
    private KeyComboTextField jtxtMapSelOrd4;
    @FXML
    private KeyComboTextField jtxtMapSelOrd5;
    @FXML
    private KeyComboTextField jtxtMapSelOrd6;
    @FXML
    private KeyComboTextField jtxtMapSelOrd7;
    @FXML
    private KeyComboTextField jtxtMapSelOrd8;
    @FXML
    private KeyComboTextField jtxtMapComplete;
    @FXML
    private KeyComboTextField jtxtMapRecall;
    @FXML
    private KeyComboTextField jtxtMapExit;


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
        historyCount.valueProperty().addListener(dirty);
        jchcExitAction.valueProperty().addListener(dirty);
        jtxtMapSelOrd1.textProperty().addListener(dirty);
        jtxtMapSelOrd2.textProperty().addListener(dirty);
        jtxtMapSelOrd3.textProperty().addListener(dirty);
        jtxtMapSelOrd4.textProperty().addListener(dirty);
        jtxtMapSelOrd5.textProperty().addListener(dirty);
        jtxtMapSelOrd6.textProperty().addListener(dirty);
        jtxtMapSelOrd7.textProperty().addListener(dirty);
        jtxtMapSelOrd8.textProperty().addListener(dirty);
        jtxtMapComplete.textProperty().addListener(dirty);
        jtxtMapRecall.textProperty().addListener(dirty);
        jtxtMapExit.textProperty().addListener(dirty);        
        jSecondscr.selectedProperty().addListener(dirty);
        
        jcboDBDriver.setOnAction(e -> {
            if ("Apache Derby Client/Server".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("org.apache.derby.jdbc.ClientDriver");
                jtxtDbURL.setText("jdbc:derby://localhost:1527/chromispos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.DerbyDialect");
            } else if ("MySQL".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("com.mysql.jdbc.Driver");
                jtxtDbURL.setText("jdbc:mysql://localhost:3306/chromispos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.MySQLDialect");
            } else if ("Oracle 11g Express".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("oracle.jdbc.driver.OracleDriver");
                jtxtDbURL.setText("jdbc:oracle:thin://localhost:1521/chromispos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                jtxtDialect.setText("org.hibernate.dialect.OracleDialect");
            } else if ("PostgreSQL".equals(jcboDBDriver.getValue())) {
                displayNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1));
                jtxtDbDriver.setText("org.postgresql.Driver");
                jtxtDbURL.setText("jdbc:postgresql://localhost:5432/chromispos");
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
        
        jchcExitAction.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (oldValue != newValue) {
                    selectedExitActionIndex = newValue.intValue();
                    switch(newValue.intValue()) {
                        case 0:  // Do not perform additional action
                            break;
                        case 1:  // Prompt for action
                            break;
                        case 2:  // Automatically close orders for entire kitchen
                            break;
                        case 3:  // Automatically close orders for this display only
                            break;
                    }
                }
            }
        });

	
        loadProperties();

    }

    public void loadProperties() {
        jcboDBDriver.setValue(AppConfig.getInstance().getProperty("db.engine"));
        jSecondscr.setSelected(Boolean.valueOf(AppConfig.getInstance().getProperty("screen.secondscr")));
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

        strHistoryCount = (AppConfig.getInstance().getProperty("recall.historycount"));
        if (strHistoryCount == null || "".equals(strHistoryCount)) {
            historyCount.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));
        } else {
            historyCount.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, Integer.parseInt(strHistoryCount)));
        }
	
	
        if (jtxtWidth.getText() == null || "".equals(jtxtWidth.getText())) {
            jtxtWidth.setText("1024");
            jtxtHeight.setText("768");
        }

        jtxtClockFormat.setText(AppConfig.getInstance().getProperty("clock.time"));
        
        String exitAction = AppConfig.getInstance().getProperty("misc.exitaction");
        if (exitAction == null || "".equals(exitAction))
            jchcExitAction.getSelectionModel().select(1);
        else
            jchcExitAction.getSelectionModel().select(Integer.parseInt(exitAction));

        
        // Get the kep mapping fields
        try {
            keyComboSelOrd1 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord1"));
        } catch(Exception ex) {
            keyComboSelOrd1 = new KeyCodeCombination(KeyCode.DIGIT1);
        }
        try {
            keyComboSelOrd2 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord2"));
        } catch(Exception ex) {
            keyComboSelOrd2 = new KeyCodeCombination(KeyCode.DIGIT2);
        }
        try {
            keyComboSelOrd3 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord3"));
        } catch(Exception ex) {
            keyComboSelOrd3 = new KeyCodeCombination(KeyCode.DIGIT3);
        }
        try {
            keyComboSelOrd4 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord4"));
        } catch(Exception ex) {
            keyComboSelOrd4 = new KeyCodeCombination(KeyCode.DIGIT4);
        }
        try {
            keyComboSelOrd5 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord5"));
        } catch(Exception ex) {
            keyComboSelOrd5 = new KeyCodeCombination(KeyCode.DIGIT5);
        }
        try {
            keyComboSelOrd6 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord6"));
        } catch(Exception ex) {
            keyComboSelOrd6 = new KeyCodeCombination(KeyCode.DIGIT6);
        }
        try {
            keyComboSelOrd7 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord7"));
        } catch(Exception ex) {
            keyComboSelOrd7 = new KeyCodeCombination(KeyCode.DIGIT7);
        }
        try {
            keyComboSelOrd8 = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.selord8"));
        } catch(Exception ex) {
            keyComboSelOrd8 = new KeyCodeCombination(KeyCode.DIGIT8);
        }
        try {
            keyComboComplete = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.complete"));
        } catch(Exception ex) {
            keyComboComplete = new KeyCodeCombination(KeyCode.ENTER);
        }
        try {
            keyComboRecall = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.recall"));
        } catch(Exception ex) {
            keyComboRecall = new KeyCodeCombination(KeyCode.R);
        }
        try {
            keyComboExit = (KeyCodeCombination) KeyCodeCombination.valueOf(AppConfig.getInstance().getProperty("keymap.exit"));
        } catch(Exception ex) {
            keyComboExit = new KeyCodeCombination(KeyCode.ENTER, ModifierValue.UP, ModifierValue.DOWN, ModifierValue.UP, ModifierValue.UP, ModifierValue.ANY);
        }
        jtxtMapSelOrd1.setKeyCodeCombination(keyComboSelOrd1);
        jtxtMapSelOrd2.setKeyCodeCombination(keyComboSelOrd2);
        jtxtMapSelOrd3.setKeyCodeCombination(keyComboSelOrd3);
        jtxtMapSelOrd4.setKeyCodeCombination(keyComboSelOrd4);
        jtxtMapSelOrd5.setKeyCodeCombination(keyComboSelOrd5);
        jtxtMapSelOrd6.setKeyCodeCombination(keyComboSelOrd6);
        jtxtMapSelOrd7.setKeyCodeCombination(keyComboSelOrd7);
        jtxtMapSelOrd8.setKeyCodeCombination(keyComboSelOrd8);
        jtxtMapComplete.setKeyCodeCombination(keyComboComplete);
        jtxtMapRecall.setKeyCodeCombination(keyComboRecall);
        jtxtMapExit.setKeyCodeCombination(keyComboExit);
        
        dirty.resetDirty();
        
    }

    public void handleSaveClick() throws IOException, LiquibaseException {
        AppConfig.getInstance().setProperty("screen.secondscr", Boolean.toString(jSecondscr.isSelected()));
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

        AppConfig.getInstance().setProperty("recall.historycount", historyCount.getValue().toString());
        
        if(selectedExitActionIndex != null) {
            AppConfig.getInstance().setProperty("misc.exitaction", selectedExitActionIndex.toString());
        }
        
        // Save the keyboard mappings
        String testString = jtxtMapSelOrd1.getKeyCodeCombination().toString();
        AppConfig.getInstance().setProperty("keymap.selord1", jtxtMapSelOrd1.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord2", jtxtMapSelOrd2.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord3", jtxtMapSelOrd3.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord4", jtxtMapSelOrd4.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord5", jtxtMapSelOrd5.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord6", jtxtMapSelOrd6.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord7", jtxtMapSelOrd7.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.selord8", jtxtMapSelOrd8.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.complete", jtxtMapComplete.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.exit", jtxtMapExit.getKeyCodeCombination().toString());
        AppConfig.getInstance().setProperty("keymap.recall", jtxtMapRecall.getKeyCodeCombination().toString());
        
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
                String changelog = "uk/chromis/configuration/kitchentable.xml";
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
                liquibase.update("implement");
            } catch (DatabaseException e) {
            }
        }

    }

    public void handleExitClick() throws IOException, LiquibaseException {                    
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
        String regex = "^$|[0-9]+";
        return number.matches(regex);
    }
}
