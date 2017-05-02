/*
 * Created by Abhiroop Singh on 2017.05.01  * 
 * Copyright © 2017 Abhiroop Singh. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.UserEvents;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Abhiroop Singh
 */
@Stateless
public class UserEventsFacade extends AbstractFacade<UserEvents> {

    @PersistenceContext(unitName = "CloudWeatherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserEventsFacade() {
        super(UserEvents.class);
    }

    /**
     *
     * @param userID is the Primary Key of the user entity in the database
     * @return a list of object references of userEvents that belong to the user
     * whose DB Primary Key = userID
     */
    public List<UserEvents> findUserEventsByUserID(Integer userID) {
        /*
        The following @NamedQuery definition is given in UserEvent.java entity class file:
        @NamedQuery(name = "UserFile.findUserEventsByUserId", query = "SELECT u FROM UserEvents u WHERE u.userId.id = :userId")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserEvents> userEvent = em.createNamedQuery("UserEvents.findUserEventsByUserId")
                .setParameter("userId", userID)
                .getResultList();

        return userEvent;
    }

    /**
     * Method used to find the Latitude for an event through the provided
     * parameters, and uses the UserEvents.findLatitude named query.
     */
    public float findLatitude(int userId, String name, long start, long end) {

        float latitude = (float) em.createNamedQuery("UserEvents.findLatitude")
                .setParameter("userId", userId)
                .setParameter("event_name", name)
                .setParameter("start_time", start)
                .setParameter("end_time", end)
                .getSingleResult();

        return latitude;

    }

    /**
     * Method used to find the Longitude for an event through the provided
     * parameters, and uses the UserEvents.findLongitude named query.
     */
    public float findLongitude(int userId, String name, long start, long end) {

        float longitude = (float) em.createNamedQuery("UserEvents.findLongitude")
                .setParameter("userId", userId)
                .setParameter("event_name", name)
                .setParameter("start_time", start)
                .setParameter("end_time", end)
                .getSingleResult();

        return longitude;

    }

    /**
     * Method used to find an event associated with the provided
     * parameters, and uses the UserEvents.findEvent named query.
     */
    public UserEvents findEvent(int userId, String name, long start, long end) {

        UserEvents event = (UserEvents) em.createNamedQuery("UserEvents.findEvent")
                .setParameter("userId", userId)
                .setParameter("event_name", name)
                .setParameter("start_time", start)
                .setParameter("end_time", end)
                .getSingleResult();

        return event;

    }
}
