/*
 * Created by Andy Sin on 2017.03.12  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class to store multi-day forecasts.
 * @author Andy
 */
public class WeatherForecast {
    private List<WeatherTimestamp> weather;
    
    public WeatherForecast() {
        weather = new ArrayList<WeatherTimestamp>();
    }
    
    public void addWeather(WeatherTimestamp newWeather) {
        weather.add(newWeather);
    }
}
