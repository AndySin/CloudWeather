package com.mycompany.jsfclasses;

import com.mycompany.WeatherSearch.SearchedWeatherController;
import com.mycompany.entityclasses.User;
import com.mycompany.entityclasses.UserEvents;
import com.mycompany.jsfclasses.util.JsfUtil;
import com.mycompany.jsfclasses.util.JsfUtil.PersistAction;
import com.mycompany.managers.AccountManager;
import com.mycompany.sessionbeans.UserEventsFacade;
import com.mycompany.sessionbeans.UserFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("userEventsController")
@SessionScoped
public class UserEventsController implements Serializable {

    @EJB
    private com.mycompany.sessionbeans.UserEventsFacade ejbFacade;

    @Inject
    private AccountManager accountManager;

    @EJB
    private UserFacade userFacade;

    @EJB
    private UserEventsFacade userEventsFacade;

    @Inject
    private UserEventsController userEventsController;

    @Inject
    private SearchedWeatherController searchedWeatherController;
    
    @Inject
    private Scheduler scheduleView;

    private List<UserEvents> items = null;
    private UserEvents selected;

    public UserEventsController() {
    }

    public UserEvents getSelected() {
        return selected;
    }

    public void setSelected(UserEvents selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserEventsFacade getFacade() {
        return ejbFacade;
    }

    public UserEvents prepareCreate() {
        selected = new UserEvents();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserEventsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserEventsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserEventsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<UserEvents> getItems() {

        if (items == null) {
            // Obtain the signed-in user's username
            String usernameOfSignedInUser = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            // Obtain the object reference of the signed-in user
            User signedInUser = getUserFacade().findByUsername(usernameOfSignedInUser);

            // Obtain the id (primary key in the database) of the signedInUser object
            Integer userId = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            items = getUserEventsFacade().findUserVideosByUserID(userId);
        }
        return items;

    }

    public void refreshFileList() {
        /*
        By setting the items to null, we force the getItems
        method above to retrieve all of the user's files again.
         */
        items = null;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public UserEventsFacade getUserEventsFacade() {
        return userEventsFacade;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public UserEvents getUserEvents(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<UserEvents> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<UserEvents> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public String addEvent() {
        if (accountManager.isLoggedIn()) {
            String eventName = searchedWeatherController.getEventName();
            float latitude = Float.valueOf(searchedWeatherController.getSearchLatitude());
            float longitude = Float.valueOf(searchedWeatherController.getSearchLongitude());
            long unixStart = searchedWeatherController.getEventStartTime().getTime();
            long unixEnd = searchedWeatherController.getEventEndTime().getTime();
            User user = accountManager.getSelected();

            // in weeks
            int duration = Integer.parseInt(searchedWeatherController.getDuration());

            switch (searchedWeatherController.getRecurring()) {
                case "weekly":
                    for (int k = 0; k <= duration; k++) {
                        Date startTime = new Date(unixStart);
                        Date endTime = new Date(unixEnd);

                        UserEvents event = new UserEvents(eventName, latitude, longitude, startTime, endTime, user);

                        userEventsFacade.create(event);

                        userEventsController.refreshFileList();

                        unixStart += 86400 * 1000 * 7;
                        unixEnd += 86400 * 1000 * 7;
                    }
                    break;
                default: // daily
                    for (int k = 0; k <= 7 * duration; k++) {
                        Date startTime = new Date(unixStart);
                        Date endTime = new Date(unixEnd);

                        UserEvents event = new UserEvents(eventName, latitude, longitude, startTime, endTime, user);

                        userEventsFacade.create(event);

                        userEventsController.refreshFileList();

                        unixStart += 86400 * 1000;
                        unixEnd += 86400 * 1000;
                    }
                    break;
            }
        }
        scheduleView.init();
        
        return "Planner?faces-redirect=true";
    }

    @FacesConverter(forClass = UserEvents.class)
    public static class UserEventsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserEventsController controller = (UserEventsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userEventsController");
            return controller.getUserEvents(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UserEvents) {
                UserEvents o = (UserEvents) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UserEvents.class.getName()});
                return null;
            }
        }

    }

}