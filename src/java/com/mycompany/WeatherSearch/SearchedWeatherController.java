/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.WeatherSearch;

import com.mycompany.Data.Alert;
import com.mycompany.Data.DataBlock;
import com.mycompany.Data.DataPoint;
import com.mycompany.Data.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Andy
 */
@SessionScoped

@Named(value = "searchedWeatherController")

public class SearchedWeatherController implements Serializable {

    // URL for weather API
    private final String weatherAPIUrl = "https://api.darksky.net/forecast/";

    // API Key for weather API
    private final String weatherAPIKey = "118cd3bc23f9cedca19972cf3c3e9347";

    private String searchLatitude;
    private String searchLongitude;

    // Object returned from API call for weather forecast
    private Response result;

    // set by user in web form
    private Date eventStartTime;
    private Date eventEndTime;

    private List<DataPoint> eventHourlyWeather;

    private static final String CURRENT = "currently";
    private static final String MINUTE = "minutely";
    private static final String HOUR = "hourly";
    private static final String DAY = "daily";
    private static final String ALERT = "alerts";
    private static final String DATA = "data";

    public String getForecast() {
        long unixStart = eventStartTime.getTime() / 1000;
        long unixEnd = eventEndTime.getTime() / 1000;
        try {
            eventHourlyWeather = new ArrayList<>();
            for (long x = unixStart; x <= unixEnd; x += 86400) {
                getDayForecast(x);
                processHourlyData();
            }
            if (eventHourlyWeather.get(eventHourlyWeather.size() - 1).getTime() < eventEndTime.
                    getTime()) {
                getDayForecast(eventEndTime.getTime() / 1000);
                processHourlyData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "WeatherForecastResultsError?faces-redirect=true";
        }
        return "WeatherForecastResults?faces-redirect=true";
    }

    private void getDayForecast(long unixTime) {
        // TODO: error handling if past date is provided by user

        try {
            String weatherAPICall = weatherAPIUrl + weatherAPIKey
                    + "/" + searchLatitude + "," + searchLongitude + ","
                    + unixTime;
            JSONObject jsonData = readUrlContent(weatherAPICall);

            result = createResponse(jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a list filled with hourly info appropriate for the event
     */
    private void processHourlyData() {
        List<DataPoint> hours = result.getHourly().getData();

        // iterate through hourly data for the event's day
        for (DataPoint datapoint : hours) {
            if (datapoint.getDate().compareTo(eventEndTime) > 0) {
                break;
            }
            if (datapoint.getDate().compareTo(eventStartTime) >= 0 && datapoint.
                    getDate().compareTo(eventEndTime) <= 0) {
                eventHourlyWeather.add(datapoint);
            }
        }
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

    private DataPoint createDataPoint(JSONObject data) {
        Double apparentTemperature = data.isNull("apparentTemperature")
                ? null : data.getDouble("apparentTemperature");
        Double apparentTemperatureMax = data.isNull("apparentTemperatureMax")
                ? null : data.getDouble("apparentTemperatureMax");
        Long apparentTemperatureMaxTime = data.isNull(
                "apparentTemperatureMaxTime")
                        ? null : data.getLong("apparentTemperatureMaxTime");
        Double apparentTemperatureMin = data.isNull("apparentTemperatureMin")
                ? null : data.getDouble("apparentTemperatureMin");
        Long apparentTemperatureMinTime = data.isNull(
                "apparentTemperatureMinTime")
                        ? null : data.getLong("apparentTemperatureMinTime");
        Double cloudCover = data.isNull("cloudCover")
                ? null : data.getDouble("cloudCover");
        Double dewPoint = data.isNull("dewPoint")
                ? null : data.getDouble("dewPoint");
        Double humidity = data.isNull("humidity")
                ? null : data.getDouble("humidity");
        String icon = data.isNull("icon")
                ? null : data.getString("icon");
        Double precipProbability = data.isNull("precipProbability")
                ? null : data.getDouble("precipProbability");
        String precipType = data.isNull("precipType")
                ? null : data.getString("precipType");
        String summary = data.isNull("summary")
                ? null : data.getString("summary");
        Double temperature = data.isNull("temperature")
                ? null : data.getDouble("temperature");
        Double temperatureMax = data.isNull("temperatureMax")
                ? null : data.getDouble("temperatureMax");
        Long temperatureMaxTime = data.isNull("temperatureMaxTime")
                ? null : data.getLong("temperatureMaxTime");
        Double temperatureMin = data.isNull("temperatureMin")
                ? null : data.getDouble("temperatureMin");
        Long temperatureMinTime = data.isNull("temperatureMinTime")
                ? null : data.getLong("temperatureMinTime");
        Long time = data.isNull("time")
                ? null : data.getLong("time");
        Double visibility = data.isNull("visibility")
                ? null : data.getDouble("visibility");
        Integer windBearing = data.isNull("windBearing")
                ? null : data.getInt("windBearing");
        Double windSpeed = data.isNull("windSpeed")
                ? null : data.getDouble("windSpeed");
        return new DataPoint(apparentTemperature, apparentTemperatureMax,
                apparentTemperatureMaxTime, apparentTemperatureMin,
                apparentTemperatureMinTime, cloudCover, dewPoint, humidity,
                icon, precipProbability, precipType, summary, temperature,
                temperatureMax, temperatureMaxTime, temperatureMin,
                temperatureMinTime, time, visibility, windBearing, windSpeed);
    }

    private DataBlock createDataBlock(JSONObject data) {
        List<DataPoint> dataList = new ArrayList<>();
        JSONArray dataArr = data.isNull(DATA)
                ? new JSONArray() : data.getJSONArray(DATA);
        for (int x = 0; x < dataArr.length(); x++) {
            dataList.add(createDataPoint(dataArr.getJSONObject(x)));
        }
        String icon = data.isNull("icon") ? null : data.getString("icon");
        String summary = data.isNull("summary")
                ? null : data.getString("summary");
        return new DataBlock(dataList, icon, summary);
    }

    private Alert createAlert(JSONObject data) {
        JSONArray regionArr = data.isNull("regions")
                ? new JSONArray() : data.getJSONArray("regions");
        List<String> regions = new ArrayList<>();
        for (int x = 0; x < regionArr.length(); x++) {
            regions.add(regionArr.getString(x));
        }
        String description = data.isNull("description")
                ? null : data.getString("description");
        Long expires = data.isNull("expires")
                ? null : data.getLong("expires");
        String severity = data.isNull("severity")
                ? null : data.getString("severity");
        Long time = data.isNull("time")
                ? null : data.getLong("time");
        String title = data.isNull("title")
                ? null : data.getString("title");
        String uri = data.isNull("uri")
                ? null : data.getString("uri");
        return new Alert(description, expires, regions, severity, time, title,
                uri);
    }

    private List<Alert> createAlerts(JSONArray data) {
        List<Alert> alerts = new ArrayList<>();
        for (int x = 0; x < data.length(); x++) {
            alerts.add(createAlert(data.getJSONObject(x)));
        }
        return alerts;
    }

    private Response createResponse(JSONObject data) {
        Double latitude = data.isNull("latitude")
                ? null : data.getDouble("latitude");
        Double longitude = data.isNull("longitude")
                ? null : data.getDouble("longitude");
        String timezone = data.isNull("timezone")
                ? null : data.getString("timezone");
        DataPoint currently = data.isNull(CURRENT)
                ? null : createDataPoint(data.getJSONObject(CURRENT));
        DataBlock minutely = data.isNull(MINUTE)
                ? null : createDataBlock(data.getJSONObject(MINUTE));
        DataBlock hourly = data.isNull(HOUR)
                ? null : createDataBlock(data.getJSONObject(HOUR));
        DataBlock daily = data.isNull(DAY)
                ? null : createDataBlock(data.getJSONObject(DAY));
        List<Alert> alerts = data.isNull(ALERT)
                ? null : createAlerts(data.getJSONArray(ALERT));
        return new Response(latitude, longitude, timezone, currently,
                minutely, hourly, daily, alerts);
    }

    public String getSearchLatitude() {
        return searchLatitude;
    }

    public String getSearchLongitude() {
        return searchLongitude;
    }

    public Response getResult() {
        return result;
    }

    public void setSearchLatitude(String searchLatitude) {
        this.searchLatitude = searchLatitude;
    }

    public void setSearchLongitude(String searchLongitude) {
        this.searchLongitude = searchLongitude;
    }

    public void setResult(Response result) {
        this.result = result;
    }

    public Date getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public List<DataPoint> getEventHourlyWeather() {
        return eventHourlyWeather;
    }
}
