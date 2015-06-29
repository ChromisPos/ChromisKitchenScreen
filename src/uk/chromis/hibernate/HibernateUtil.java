package uk.chromis.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import uk.chromis.dto.Orders;

import uk.chromis.forms.AppConfig;
import uk.chromis.utils.AltEncrypter;

public class HibernateUtil {

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
        configuration.setProperty("hibernate.c3p0.initialPoolSize", "5");
        configuration.setProperty("hibernate.c3p0.min", "5");
        configuration.setProperty("hibernate.c3p0.max", "15");
        configuration.setProperty("hibernate.c3p0.timeout", "5000");
        configuration.setProperty("hibernate.c3p0.max_statements", "0");
        configuration.setProperty("hibernate.c3p0.idle_test_period", "300");
        configuration.setProperty("hibernate.c3p0.aquire_increment", "2");

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.connection.pool_size", "0");
        configuration.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");

        configuration.addAnnotatedClass(Orders.class);
       

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
