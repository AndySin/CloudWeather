package com.mycompany.jsfclasses;

import com.mycompany.WeatherSearch.SearchedWeatherController;
import com.mycompany.entityclasses.UserEvents;
import com.mycompany.jsfclasses.UserEventsController;
import com.mycompany.managers.AccountManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@SessionScoped

@Named(value = "scheduleView")

public class Scheduler implements Serializable {

    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    @Inject
    private UserEventsController userEventsController;

    @Inject
    private AccountManager accountManager;

    @Inject
    private SearchedWeatherController searchedWeatherController;

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();

        userEventsController.refreshFileList();

        for (UserEvents events : userEventsController.getItems()) {

            Date start = new Date(events.getStartTime());
            Date end = new Date(events.getEndTime());
            eventModel.addEvent(new DefaultScheduleEvent(events.getEventName(), start, end));

        }

    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(ActionEvent actionEvent) {

        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }

        event = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) throws IOException {
        event = (ScheduleEvent) selectEvent.getObject();

        String event_name = event.getTitle();
        long start = event.getStartDate().getTime();
        long end = event.getEndDate().getTime();

        String username = accountManager.getSelected().getUsername();
        int userId = accountManager.getUserFacade().findByUsername(username).getId();

        float latitude = userEventsController.getUserEventsFacade().findLatitude(userId, event_name, start, end);
        float longitude = userEventsController.getUserEventsFacade().findLongitude(userId, event_name, start, end);

        searchedWeatherController.setEventName(event_name);
        searchedWeatherController.setEventStartTime(event.getStartDate());
        searchedWeatherController.setEventEndTime(event.getEndDate());

        String lat = String.valueOf(latitude);
        String longi = String.valueOf(longitude);

        searchedWeatherController.setSearchLatitude(lat);
        searchedWeatherController.setSearchLongitude(longi);

        searchedWeatherController.getForecast();
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("PlannerResults.xhtml");
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
