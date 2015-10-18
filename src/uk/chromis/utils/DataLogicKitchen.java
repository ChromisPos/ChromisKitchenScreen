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

package uk.chromis.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import uk.chromis.dto.Orders;
import uk.chromis.forms.AppConfig;
import uk.chromis.hibernate.HibernateUtil;

public class DataLogicKitchen {

    private Session session;
    private String sql_query;
    private Query query;

    public DataLogicKitchen() {
    }

    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public List<String> readDistinctOrders() {
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            sql_query = "SELECT DISTINCT ORDERID, ORDERTIME FROM ORDERS ORDER BY ORDERTIME ";
        } else {
            sql_query = "SELECT DISTINCT ORDERID, ORDERTIME FROM ORDERS WHERE DISPLAYID = " + Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")) + " ORDER BY ORDERTIME";
        }
        SQLQuery query = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        query.addScalar("ORDERID");
        List results = query.list();
        results = new ArrayList<String>(new LinkedHashSet<String>(results));
        return results;
    }

    public void removeOrder(String orderid) {
        init();
        session.beginTransaction();
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            query = session.createQuery("DELETE FROM ORDERS WHERE ORDERID = :id ");
        } else {
            query = session.createQuery("DELETE FROM ORDERS WHERE ORDERID = :id AND DISPLAYID = :display");
            query.setParameter("display", Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")));
        }

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
    
    /**
     * Remove all orders for current display only
     */
    public void removeAllOrdersDisplay() {
        init();
        session.beginTransaction();
        Query query = session.createQuery("DELETE FROM ORDERS WHERE DISPLAYID = :display");
        query.setParameter("display", Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")));
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<Orders> selectByOrderId(String orderid) {
        if (Boolean.valueOf(AppConfig.getInstance().getProperty("screen.allorders"))) {
            sql_query = "SELECT * FROM ORDERS WHERE ORDERID ='" + orderid + "' ORDER BY AUXILIARY ";
        } else {
            sql_query = "SELECT * FROM ORDERS WHERE ORDERID ='" + orderid + "' AND DISPLAYID = " + Integer.parseInt(AppConfig.getInstance().getProperty("screen.displaynumber")) + " ORDER BY AUXILIARY ";
        }

        SQLQuery query = HibernateUtil.getSessionFactory().openSession().createSQLQuery(sql_query);
        query.addEntity(Orders.class);
        List<Orders> results = query.list();
        return results;
    }
	 
	 
	 /* N Deppe Sept 2015 - Added to be able to create new order records for recall function */
	 public void createOrder(Orders orderData) {
		String newId = UUID.randomUUID().toString();
		sql_query = "INSERT INTO ORDERS (ID, ORDERID, QTY, DETAILS, ATTRIBUTES, NOTES, TICKETID, ORDERTIME, DISPLAYID, AUXILIARY)"
					  + " VALUES ( :id, :orderid, :qty, :details, :attributes, :notes, :ticketid, :ordertime, :displayid, :auxiliaryid )";
		init();
		session.beginTransaction();
		query = session.createSQLQuery(sql_query);
		query.setParameter("id", newId);
		query.setParameter("orderid", orderData.getOrderid());
		query.setParameter("qty", orderData.getQty());
		query.setParameter("details", orderData.getDetails());
		query.setParameter("attributes", orderData.getAttributes());
		query.setParameter("notes", orderData.getNotes());
		query.setParameter("ticketid", orderData.getTicketid());
		query.setParameter("ordertime", orderData.getOrdertime());
		query.setParameter("displayid", orderData.getDisplayid());
                query.setParameter("auxiliaryid", orderData.getAuxiliary());
		int result = query.executeUpdate();
		session.getTransaction().commit();
		session.close();		 
	}
	 

}
