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

package uk.chromis.dto;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ORDERS")
public class Orders {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "ORDERID")
    private String orderid;
    @Column(name = "QTY")
    private Integer qty;
    @Column(name = "DETAILS")
    private String details;
    @Column(name = "ATTRIBUTES")
    private String attributes;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "TICKETID")
    private String ticketid;
    @Column(name = "ORDERTIME")
    private Timestamp ordertime;
    @Column(name = "DISPLAYID")
    private Integer displayid;

    /**
     * @return the orderid
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    /**
     * @return the qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the ticketid
     */
    public String getTicketid() {
        return ticketid;
    }

    /**
     * @param ticketid the ticketid to set
     */
    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    /**
     * @return the ordertime
     */
    public Timestamp getOrdertime() {
        return ordertime;
    }

    /**
     * @param ordertime the ordertime to set
     */
    public void setOrdertime(Timestamp ordertime) {
        this.ordertime = ordertime;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the displayid
     */
    public Integer getDisplayid() {
        return displayid;
    }

    /**
     * @param displayid the displayid to set
     */
    public void setDisplayid(Integer displayid) {
        this.displayid = displayid;
    }



}
