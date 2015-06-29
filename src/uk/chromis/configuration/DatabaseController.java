/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.AltEncrypter;
import uk.chromis.utils.DirtyManager;

/**
 * FXML Controller class
 *
 */
public class DatabaseController implements Initializable {

    public ComboBox<String> jcboDBDriver;
    public TextField jtxtDbDriverLib;
    public TextField jtxtDbDriver;
    public TextField jtxtDbURL;
    public TextField jtxtDbUser;
    public TextField jtxtDbPassword;
    public Button save;

    private File file;
    private final DirtyManager dirty = new DirtyManager();
    private Connection conn;
    private String dialect;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        jcboDBDriver.valueProperty().addListener(dirty);
        jtxtDbDriverLib.textProperty().addListener(dirty);
        jtxtDbDriver.textProperty().addListener(dirty);
        jtxtDbURL.textProperty().addListener(dirty);
        jtxtDbUser.textProperty().addListener(dirty);
        jtxtDbPassword.textProperty().addListener(dirty);

        loadProperties();

        jcboDBDriver.setOnAction(e -> {
            if ("Apache Derby Client/Server".equals(jcboDBDriver.getValue())) {
                jtxtDbDriverLib.setText(new File(new File(AppConfig.getInstance().getDirPath()), "lib/derbyclient.jar").getAbsolutePath());
                jtxtDbDriver.setText("org.apache.derby.jdbc.ClientDriver");
                jtxtDbURL.setText("jdbc:derby://localhost:1527/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                dialect = "org.hibernate.dialect.DerbyDialect";
            } else if ("MySQL".equals(jcboDBDriver.getValue())) {
                jtxtDbDriverLib.setText(new File(new File(AppConfig.getInstance().getDirPath()), "lib/mysql-connector-java-5.1.26-bin.jar").getAbsolutePath());
                jtxtDbDriver.setText("com.mysql.jdbc.Driver");
                jtxtDbURL.setText("jdbc:mysql://localhost:3306/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                dialect = "org.hibernate.dialect.MySQLDialect";
            } else if ("Oracle 11g Express".equals(jcboDBDriver.getValue())) {
                jtxtDbDriverLib.setText(new File(new File(AppConfig.getInstance().getDirPath()), "lib/ojdbc6.jar").getAbsolutePath());
                jtxtDbDriver.setText("oracle.jdbc.driver.OracleDriver");
                jtxtDbURL.setText("jdbc:oracle:thin://localhost:1521/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                dialect = "org.hibernate.dialect.OracleDialect";
            } else if ("PostgreSQL".equals(jcboDBDriver.getValue())) {
                jtxtDbDriverLib.setText(new File(new File(AppConfig.getInstance().getDirPath()), "lib/postgresql-9.2-1003.jdbc4.jar").getAbsolutePath());
                jtxtDbDriver.setText("org.postgresql.Driver");
                jtxtDbURL.setText("jdbc:postgresql://localhost:5432/unicentaopos");
                jtxtDbUser.setText("");
                jtxtDbPassword.setText("");
                dialect = "org.hibernate.dialect.PostgreSQLDialect";
            }
        });
    }

    public void loadProperties() {
        jcboDBDriver.setValue(AppConfig.getInstance().getProperty("db.engine"));
        jtxtDbDriverLib.setText(AppConfig.getInstance().getProperty("db.driverlib"));
        jtxtDbDriver.setText(AppConfig.getInstance().getProperty("db.driver"));
        jtxtDbURL.setText(AppConfig.getInstance().getProperty("db.URL"));
        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        jtxtDbUser.setText(sDBUser);
        jtxtDbPassword.setText(sDBPassword);
        dirty.resetDirty();
    }

    public void handleSaveClick() throws IOException {
        AppConfig.getInstance().setProperty("db.engine", jcboDBDriver.getValue());
        AppConfig.getInstance().setProperty("db.driverlib", jtxtDbDriverLib.getText());
        AppConfig.getInstance().setProperty("db.driver", jtxtDbDriver.getText());
        AppConfig.getInstance().setProperty("db.URL", jtxtDbURL.getText());
        AppConfig.getInstance().setProperty("db.user", jtxtDbUser.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtDbUser.getText());
        AppConfig.getInstance().setProperty("db.password", "crypt:" + cypher.encrypt(new String(jtxtDbPassword.getText())));
        AppConfig.getInstance().setProperty("db.dialect", dialect);
        
        AppConfig.getInstance().save();
        dirty.resetDirty();
    }

    public void fileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Driver File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            jtxtDbDriverLib.setText(file.toString());
        }
    }

    public void handleExitClick() throws IOException {
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


}
