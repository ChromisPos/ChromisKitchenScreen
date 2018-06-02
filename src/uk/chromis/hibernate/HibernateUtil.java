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
package uk.chromis.hibernate;


import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.utils.AltEncrypter;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
       
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DRIVER,AppConfig.getInstance().getProperty("db.driver"));
                settings.put(Environment.URL,  AppConfig.getInstance().getProperty("db.URL")+ "?useSSL=false");
                settings.put(Environment.USER, AppConfig.getInstance().getProperty("db.user"));
                settings.put(Environment.PASS, sDBPassword);
                settings.put(Environment.HBM2DDL_AUTO, "update");
                settings.put(Environment.SHOW_SQL, false);

                // c3p0 configuration
                settings.put(Environment.C3P0_MIN_SIZE, 5);         //Minimum size of pool
                settings.put(Environment.C3P0_MAX_SIZE, 20);        //Maximum size of pool
                settings.put(Environment.C3P0_ACQUIRE_INCREMENT, 1);//Number of connections acquired at a time when pool is exhausted 
                settings.put(Environment.C3P0_TIMEOUT, 1800);       //Connection idle time
                settings.put(Environment.C3P0_MAX_STATEMENTS, 150); //PreparedStatement cache size

                settings.put(Environment.C3P0_CONFIG_PREFIX + ".initialPoolSize", 5);

                registryBuilder.applySettings(settings);

                registry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Orders.class);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

/*
    private static SessionFactory sessionFactory = buildSessionFactory();
    private static ServiceRegistry serviceRegistry;

    private static SessionFactory buildSessionFactory() {

        String sDBUser = AppConfig.getInstance().getProperty("db.user");
        String sDBPassword = AppConfig.getInstance().getProperty("db.password");
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }

        Configuration configuration = new Configuration();

        // Set up the database details ready for the connections
        configuration.setProperty("hibernate.connection.driver_class", AppConfig.getInstance().getProperty("db.driver"));
        configuration.setProperty("hibernate.connection.url", AppConfig.getInstance().getProperty("db.URL"));
        configuration.setProperty("hibernate.connection.username", AppConfig.getInstance().getProperty("db.user"));
        configuration.setProperty("hibernate.connection.password", sDBPassword);
        configuration.setProperty("hibernate.dialect", AppConfig.getInstance().getProperty("db.dialect"));
        
         // Set up connection pooling to use c3p0 rather than hibernates built in pooling
         configuration.setProperty("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
         // configuration.setProperty("hibernate.connection.provider_class", "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
       
         //configuration.setProperty("hibernate.c3p0.initialPoolSize", "5");
         configuration.setProperty("hibernate.c3p0.min", "5");
         configuration.setProperty("hibernate.c3p0.max", "10");
         configuration.setProperty("hibernate.c3p0.timeout", "5000");
         configuration.setProperty("hibernate.c3p0.max_statements", "30");
         configuration.setProperty("hibernate.c3p0.idle_test_period", "300");
         configuration.setProperty("hibernate.c3p0.aquire_increment", "2");
         
        //configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        //configuration.setProperty("hibernate.connection.pool_size", "5");

        configuration.addAnnotatedClass(Orders.class);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            return null;
        }

        return sessionFactory;

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }*/

