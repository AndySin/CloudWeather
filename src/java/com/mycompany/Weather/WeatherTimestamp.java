/*
 * Created by Andy Sin on 2017.03.12  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adds a timestamp to the searchedWeather object.
 * Used for multi-day forecasts.
 * @author Andy
 */
public class WeatherTimestamp {
    private SearchedWeather weather;
    private String time;
    
    public WeatherTimestamp(SearchedWeather weather, long unixTime) {
        this.weather = weather;
        time = getTimeDateFormat(unixTime);
    }
    
    private String getTimeDateFormat(long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d hh:mm aaa");
        return format.format(date);
    }

    public SearchedWeather getWeather() {
        return weather;
    }

    public String getTime() {
        return time;
    }
}
