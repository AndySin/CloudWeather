/*
 * Created by Abhiroop Singh on 2017.05.01  * 
 * Copyright © 2017 Abhiroop Singh. All rights reserved. * 
 */
package com.mycompany.jsfclasses;

import com.mycompany.WeatherSearch.SearchedWeatherController;
import com.mycompany.entityclasses.UserEvents;
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
/**
 * Class used to listening to events on the planner for actions
 */
public class Scheduler implements Serializable {

    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    @Inject
    private UserEventsController userEventsController;

    @Inject
    private AccountManager accountManager;

    @Inject
    private SearchedWeatherController searchedWeatherController;

    /*
        Method used to construct the intial pre-defined events on the calendar for
        the user that is logged in. Goes through and add all the events from the database
        under that registered user.
     */
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

    /*
        Getter and setter methods
     */
    
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

    /*
     *   Method used when an Event is clicked on the Calendar, which will in turn
     *   will requery the results and show the results page for the user-selected event.
     */
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
    
    /*
     *  Method used to add events to the Calendar when a User clicks on a empty date box.
     */
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
