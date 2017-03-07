/*
 * Created by Andy Sin on 2017.03.02  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.WeatherSearch;

/**
 * A Weather object that stores data retrieved from a weather API.
 *
 * @author Andy
 */
public class SearchedWeather {

    private double kelvin;
    private int celsius;
    private int fahrenheit;

    private double latitude;
    private double longitude;

    private String weatherDescription;

    private String locationName;

    public SearchedWeather(double kelvin, double longitude, double latitude,
            String weatherDescription, String locationName) {
        this.kelvin = kelvin;
        celsius = (int)(kelvin - 273.15);
        fahrenheit = (int)((kelvin - 273.15) * 1.8 + 32);

        this.longitude = longitude;
        this.latitude = latitude;

        this.weatherDescription = weatherDescription;
        this.locationName = locationName;
    }

    public double getKelvin() {
        return kelvin;
    }

    public void setKelvin(double kelvin) {
        this.kelvin = kelvin;
    }

    public int getCelsius() {
        return celsius;
    }

    public void setCelsius(int celsius) {
        this.celsius = celsius;
    }

    public int getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(int fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
