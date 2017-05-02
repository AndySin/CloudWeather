/*
 * Created by Abhiroop Singh on 2017.04.22  * 
 * Copyright © 2017 Abhiroop Singh. All rights reserved. * 
 */
package com.mycompany.entityclasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Abhi
 */
@Entity
@Table(name = "UserEvents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserEvents.findAll", query = "SELECT u FROM UserEvents u")
    , @NamedQuery(name = "UserEvents.findById", query = "SELECT u FROM UserEvents u WHERE u.id = :id")
    , @NamedQuery(name = "UserEvents.findByEventName", query = "SELECT u FROM UserEvents u WHERE u.eventName = :eventName")
    , @NamedQuery(name = "UserEvents.findByLatitude", query = "SELECT u FROM UserEvents u WHERE u.latitude = :latitude")
    , @NamedQuery(name = "UserEvents.findByLongitude", query = "SELECT u FROM UserEvents u WHERE u.longitude = :longitude")
    , @NamedQuery(name = "UserEvents.findByStartTime", query = "SELECT u FROM UserEvents u WHERE u.startTime = :startTime")
    , @NamedQuery(name = "UserEvents.findByEndTime", query = "SELECT u FROM UserEvents u WHERE u.endTime = :endTime")
    , @NamedQuery(name = "UserEvents.findUserEventsByUserId", query = "SELECT u FROM UserEvents u WHERE u.userId.id = :userId")
    , @NamedQuery(name = "UserEvents.findLatitude", query = "Select u.latitude FROM UserEvents u WHERE u.userId.id = :userId AND u.eventName = :event_name AND u.startTime = :start_time AND u.endTime = :end_time")
    , @NamedQuery(name = "UserEvents.findLongitude", query = "Select u.longitude FROM UserEvents u WHERE u.userId.id = :userId AND u.eventName = :event_name AND u.startTime = :start_time AND u.endTime = :end_time")
    , @NamedQuery(name = "UserEvents.findEvent", query = "Select u FROM UserEvents u WHERE u.userId.id = :userId AND u.eventName = :event_name AND u.startTime = :start_time AND u.endTime = :end_time")})

public class UserEvents implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    private long startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    private long endTime;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "event_name")
    private String eventName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private float longitude;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    /*
    ===============================================================
    Class constructors for instantiating a UserEvent entity object to
    represent a row in the User table in the CloudWeatherDB database.
    ===============================================================
     */
    public UserEvents() {
    }

    public UserEvents(Integer id) {
        this.id = id;
    }

    public UserEvents(Integer id, String eventName, float latitude, float longitude, long startTime, long endTime) {
        this.id = id;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UserEvents(String eventName, float latitude, float longitude, long startTime, long endTime, User user) {
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = user;
    }

    /*
    ===============================================================
    Getters and setters
    ===============================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserEvents)) {
            return false;
        }
        UserEvents other = (UserEvents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entityclasses.UserEvents[ id=" + id + " ]";
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
