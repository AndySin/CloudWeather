/*
 * Created by Andy Sin on 2017.03.02  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Weather;

/**
 * A Weather object that stores data retrieved from a weather API.
 *
 * @author Andy
 */
public class SearchedWeather {

    private double kelvin;
    private int celsius;
    private int fahrenheit;

    private double kelvinMin;
    private int celsiusMin;
    private int fahrenheitMin;

    private double kelvinMax;
    private int celsiusMax;
    private int fahrenheitMax;

    private double latitude;
    private double longitude;

    private String weatherDescription;

    private String locationName;

    public SearchedWeather(double kelvin, double kelvinMin, double kelvinMax, 
            double longitude, double latitude, String weatherDescription, 
            String locationName) {
        
        setKelvin(kelvin);
        setKelvinMin(kelvinMin);
        setKelvinMax(kelvinMax);

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
        celsius = (int) (kelvin - 273.15);
        fahrenheit = (int) ((kelvin - 273.15) * 1.8 + 32);
    }

    public int getCelsius() {
        return celsius;
    }

    public int getFahrenheit() {
        return fahrenheit;
    }

    public double getKelvinMin() {
        return kelvinMin;
    }

    public void setKelvinMin(double kelvinMin) {
        this.kelvinMin = kelvinMin;
        celsiusMin = (int) (kelvinMin - 273.15);
        fahrenheitMin = (int) ((kelvinMin - 273.15) * 1.8 + 32);
    }

    public int getCelsiusMin() {
        return celsiusMin;
    }

    public int getFahrenheitMin() {
        return fahrenheitMin;
    }

    public double getKelvinMax() {
        return kelvinMax;
    }

    public void setKelvinMax(double kelvinMax) {
        this.kelvinMax = kelvinMax;
        celsiusMax = (int) (kelvinMax - 273.15);
        fahrenheitMax = (int) ((kelvinMax - 273.15) * 1.8 + 32);
    }

    public int getCelsiusMax() {
        return celsiusMax;
    }

    public int getFahrenheitMax() {
        return fahrenheitMax;
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
