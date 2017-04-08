/*
 * Created by Andy Sin on 2017.03.02  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.WeatherSearch;

import com.mycompany.Weather.SearchedWeather;
import com.mycompany.Weather.WeatherForecast;
import com.mycompany.Weather.WeatherTimestamp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Andy
 */
@SessionScoped

@Named(value = "searchedWeatherControllerOld")

public class SearchedWeatherControllerOld implements Serializable {

    // URL for weather API
    private final String weatherAPIUrl = "http://api.openweathermap.org/data/2.5/";

    // API Key for weather API
    private final String weatherAPIKey = "4c99a07ca200047bf938adedb4a7891e";

    private final String COORD = "coord";
    private final String WEATHER = "weather";
    private final String BASE = "base";
    private final String MAIN = "main";
    private final String WIND = "wind";
    private final String RAIN = "rain";
    private final String CLOUDS = "clouds";
    private final String NAME = "name";
    private final String LIST = "list";
    private final String DT = "dt";

    private String searchParameter;
    private String searchQuery;

    // Object returned from API call for current forecast
    private SearchedWeather searchResults;

    // Object returned from API call for multi-day forecast
    private WeatherForecast forecastResults;

    public String getForecast() {

        String noSpaceSearchQuery = searchQuery.replaceAll(" ", "+");

        try {
            String weatherAPICall = weatherAPIUrl + "weather?"
                    + searchParameter + "=" + noSpaceSearchQuery
                    + "&appid=" + weatherAPIKey;
            JSONObject jsonData = readUrlContent(weatherAPICall);

            JSONObject jsonCoords = jsonData.getJSONObject(COORD);
            JSONArray jsonWeather = jsonData.getJSONArray(WEATHER);
            JSONObject jsonTemp = jsonData.getJSONObject(MAIN);

            searchResults = makeSearchedWeather(
                    jsonData, jsonCoords, jsonWeather, jsonTemp);

        } catch (Exception e) {
            e.printStackTrace();
            return "WeatherForecastResultsError?faces-redirect=true";
        }
        return "WeatherForecastResults?faces-redirect=true";
    }

    public String get5DayForecast() {
        String noSpaceSearchQuery = searchQuery.replaceAll(" ", "+");

        try {
            String weatherAPICall = weatherAPIUrl + "forecast?"
                    + searchParameter + "=" + noSpaceSearchQuery
                    + "&appid=" + weatherAPIKey;
            JSONObject jsonData = readUrlContent(weatherAPICall);

            JSONObject jsonCoords = jsonData.getJSONObject(COORD);
            JSONArray jsonForecast = jsonData.getJSONArray(LIST);
            int arrSize = jsonForecast.length();

            forecastResults = new WeatherForecast();
            for (int x = 0; x < arrSize; x++) {
                JSONObject current = jsonForecast.getJSONObject(x);
                long dt = current.getLong(DT);
                JSONArray jsonWeather = jsonData.getJSONArray(WEATHER);
                JSONObject jsonTemp = jsonData.getJSONObject(MAIN);
                SearchedWeather currentWeather = makeSearchedWeather(
                        jsonData, jsonCoords, jsonWeather, jsonTemp);
                WeatherTimestamp currentWeatherTimestamp
                        = new WeatherTimestamp(currentWeather, dt);
                forecastResults.addWeather(currentWeatherTimestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "WeatherForecastResultsError?faces-redirect=true";
        }
        return "WeatherMultiForecastResults?faces-redirect=true";
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public SearchedWeather getSearchResults() {
        return searchResults;
    }

    /**
     * Return the content of a given URL as String
     *
     * @param url URL to retrieve the JSON data from
     * @return JSON data from the given URL as String
     * @throws Exception
     */
    private JSONObject readUrlContent(String url) throws Exception {
        BufferedReader urlReader = null;
        try {
            URL weatherUrl = new URL(url);
            urlReader = new BufferedReader(
                    new InputStreamReader(weatherUrl.openStream()));
            StringBuilder JSONResult = new StringBuilder();
            int current = urlReader.read();
            while (current != -1) {
                JSONResult.append((char) current);
                current = urlReader.read();
            }
            return new JSONObject(JSONResult.toString());
        } finally {
            if (urlReader != null) {
                urlReader.close();
            }
        }
    }

    private SearchedWeather makeSearchedWeather(JSONObject jsonData,
            JSONObject jsonCoords, JSONArray jsonWeather, JSONObject jsonTemp) {
        return new SearchedWeather(jsonTemp.getDouble("temp"),
                jsonTemp.getDouble("temp_min"), jsonTemp.getDouble(
                "temp_max"),
                jsonCoords.getDouble("lon"), jsonCoords.getDouble("lat"),
                jsonWeather.getJSONObject(0).getString("description"),
                jsonData.getString("name"));
    }

}