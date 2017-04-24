/*
 * Created by Tanya Sutan-Tanon on 2017.04.18  * 
 * Copyright Â© 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.sessionbeans;

import com.mycompany.entityclasses.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tanya
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "CloudWeatherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    /*
    -----------------------------------------------------
    The following methods are added to the generated code
    -----------------------------------------------------
     */
    /**
     * @param id is the Primary Key of the User entity in a table row in the
     * CloudWeather database.
     * @return object reference of the User entity whose primary key is id
     */
    public User getUser(int id) {

        // The find method is inherited from the parent AbstractFacade class
        return em.find(User.class, id);
    }

    /**
     * @param username is the username attribute (column) value of the user
     * @return object reference of the User entity whose username is username
     */
    public User findByUsername(String username) {
        if (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                .setParameter("username", username)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (User) (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                    .setParameter("username", username)
                    .getSingleResult());
        }
    }

    /**
     * Deletes the User entity whose primary key is id
     *
     * @param id is the Primary Key of the User entity in a table row in the
     * CloudWeather database.
     */
    public void deleteUser(int id) {

        // The find method is inherited from the parent AbstractFacade class
        User user = em.find(User.class, id);

        // The remove method is inherited from the parent AbstractFacade class
        em.remove(user);
    }

}