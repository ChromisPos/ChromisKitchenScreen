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



package uk.chromis.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.hibernate.HibernateUtil;

public class DataLogicKitchen {

    private Session session;

    public DataLogicKitchen() {
    }

    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
    }


    public List<String> readDistinctOrders() {
     //   String sql_query = "SELECT DISTINCT ORDERID, ORDERTIME FROM ORDERS ORDER BY ORDERTIME ";
        String sql_query = "SELECT DISTINCT ORDERID, ORDERTIME FROM ORDERS WHERE DISPLAYID = " + Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")) + " ORDER BY ORDERTIME ";
        SQLQuery query = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        query.addScalar("ORDERID");
        List results = query.list();
        results = new ArrayList<String>(new LinkedHashSet<String>(results));
        return results;
    }

    public void removeOrder(String orderid) {
        init();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM ORDERS WHERE ORDERID = :id ");
        query.setParameter("id", orderid);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

        public void removeAllOrders() {
        init();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM ORDERS ");        
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    
    public List<Orders> selectByOrderId(String orderid) {
        String sql_query = "SELECT * FROM ORDERS WHERE ORDERID ='" + orderid + "' ";
        SQLQuery query = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        query.addEntity(Orders.class);
        List<Orders> results = query.list();
        return results;
    }

}
