/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.createtable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import liquibase.Liquibase;
import liquibase.database.*;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import uk.chromis.forms.AppConfig;
import uk.chromis.hibernate.HibernateUtil;
import uk.chromis.utils.AltEncrypter;
import uk.chromis.utils.DataLogicKitchen;

/**
 * FXML Controller class
 *
 * @author John
 */
public class CreateTableController implements Initializable {

   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void createTable() throws LiquibaseException {

        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        String url = AppConfig.getInstance().getProperty("db.URL");
        Session session = HibernateUtil.getSessionFactory().openSession();
        SessionImpl sessionImpl = (SessionImpl) session;
        
        Connection connection = sessionImpl.connection();
        try {
         //   ClassLoader cloader = new URLClassLoader(new URL[]{new File(m_props.getProperty("db.driverlib")).toURI().toURL()});
          //  DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(m_props.getProperty("db.driver"), true, cloader).newInstance()));

            String changelog = "uk/chromis/createtable/kitchentable.xml";

          //  Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(DriverManager.getConnection(url, sDBUser, sDBPassword)));
              Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            liquibase.update("implement");
        } catch (DatabaseException ex) {            
        }
    
    
    
    
    }

    
    
    
    public void handleExitClick() throws IOException {
        System.exit(0);
    }

}

