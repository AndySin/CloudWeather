/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Data;

import java.util.List;

/**
 * Contains a Dark Sky API response.
 * 
 * @author Andy
 */
public class Response {
    private final Double latitude;
    private final Double longitude;
    private final String timezone;
    private final DataPoint currently;
    private final DataBlock minutely;
    private final DataBlock hourly;
    private final DataBlock daily;
    private final List<Alert> alerts;

    public Response(Double latitude, Double longitude, String timezone,
            DataPoint currently, DataBlock minutely, DataBlock hourly,
            DataBlock daily, List<Alert> alerts) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.currently = currently;
        this.minutely = minutely;
        this.hourly = hourly;
        this.daily = daily;
        this.alerts = alerts;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public DataPoint getCurrently() {
        return currently;
    }

    public DataBlock getMinutely() {
        return minutely;
    }

    public DataBlock getHourly() {
        return hourly;
    }

    public DataBlock getDaily() {
        return daily;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }
}
