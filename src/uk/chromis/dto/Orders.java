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

package uk.chromis.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "ORDERS")
@Table(name = "ORDERS", uniqueConstraints = {
    @UniqueConstraint(columnNames = "ID")})
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, length = 50)
    private String id;

    @Column(name = "ORDERID", nullable = true, length = 50)
    private String orderid;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "DETAILS", nullable = true, length = 255)
    private String details;

    @Column(name = "ATTRIBUTES", nullable = true, length = 255)
    private String attributes;

    @Column(name = "NOTES", nullable = true, length = 255)
    private String notes;

    @Column(name = "TICKETID", nullable = true, length = 50)
    private String ticketid;

    @Column(name = "ORDERTIME", nullable = true)
    private Timestamp ordertime;

    @Column(name = "DISPLAYID", nullable = true)
    private Integer displayid;

    @Column(name = "AUXILIARY")
    private Integer auxiliaryid;

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

    public Integer getAuxiliary() {
        return auxiliaryid;
    }


    public void setAuxiliary(Integer auxiliaryid) {
        this.auxiliaryid = auxiliaryid;
    }

}
