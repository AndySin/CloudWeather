/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.WeatherSearch;

import com.mycompany.Data.Alert;
import com.mycompany.Data.DataBlock;
import com.mycompany.Data.DataPoint;
import com.mycompany.Data.Response;
import com.mycompany.managers.AccountManager;
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
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
    private final String weatherAPIKey = "4c3a261b5bb1e7aeded8500dff160cd0";

    private String searchLatitude;
    private String searchLongitude;

    // Object returned from API call for weather forecast
    private Response result;

    // set by user in web form
    private Date eventStartTime;
    private Date eventEndTime;

    private String eventName;

    private String recurring;
    private String duration;

    // hourly weather information relevant to the event is stored in here
    private List<DataPoint> eventHourlyWeather;

    // summary statistics for the event used to display in the WeatherForecastResults page
    private double maxTemp;
    private double minTemp;
    private double avgTemp;

    private double maxCloudCover;
    private double minCloudCover;
    private double avgCloudCover;

    private double maxVisibility;
    private double minVisibility;
    private double avgVisibility;

    private double avgWind;
    private double avgHumidity;
    private double avgFeelsLike;

    // icon to be used for display that best represents the weather for the duration of the event
    private String freqIcon;

    private static final String CURRENT = "currently";
    private static final String MINUTE = "minutely";
    private static final String HOUR = "hourly";
    private static final String DAY = "daily";
    private static final String ALERT = "alerts";
    private static final String DATA = "data";

    @Inject
    private AccountManager accountManager;

    // Resulting FacesMessage produced
    FacesMessage resultMsg;

    public String getForecast() {
        // .getTime() returns time in milliseconds
        long unixStart = eventStartTime.getTime() / 1000;
        long unixEnd = eventEndTime.getTime() / 1000;

        // event start/end time validation check
        if (unixStart > unixEnd) {
            resultMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please Verify Your Event End Date/Time!", null);
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return null;
        }

        // ensure only multiday event forecast check is allowed for signed in users - validation check
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        if (!accountManager.isLoggedIn() && !formatter.format(eventStartTime).
                equals(formatter.format(eventEndTime))) {
            resultMsg = new FacesMessage(
                    "Multiday Event Forecast Is Only For Registered Users. Please Register OR Sign In!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return null;
        }

        try {
            eventHourlyWeather = new ArrayList<>();
            // get weather information day by day for the event
            for (long x = unixStart; x <= unixEnd; x += 86400) {
                getDayForecast(x);
                processHourlyData();
            }
            // SPECIAL CASE: Round minutes to the associated hour
            if (eventHourlyWeather.get(eventHourlyWeather.size() - 1).getTime() + (3600 - 1) < (eventEndTime.
                    getTime() / 1000)) {
                getDayForecast(eventEndTime.getTime() / 1000);
                processHourlyData();
            }
        } catch (Exception e) {
            // something went wrong when dealing with location provided by user
            e.printStackTrace();
            resultMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Input. Please Verify Location!", null);
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return null;
        }

        // extract event specific weather information
        updateEventStats();

        // redirect page to display the results
        return "WeatherForecastResults?faces-redirect=true";
    }

    // for demo purposes - to show alerts
    public String setCurrentLocation() {
        // harcoded Golsboro, NC coordiates for user home page
        setSearchLatitude("35.381174");
        setSearchLongitude("-78.062514");
        this.getCurrentlyForecast();
        
        return "UserHomePage.xhtml?faces-redirect=true";
    }

    /**
     * Calculates summary statistics for a particular event based on hourly
     * information that has been extracted.
     */
    private void updateEventStats() {
        // reset values
        maxTemp = Double.MIN_VALUE;
        minTemp = Double.MAX_VALUE;
        avgTemp = 0;

        maxCloudCover = Double.MIN_VALUE;
        minCloudCover = Double.MAX_VALUE;
        avgCloudCover = 0;

        maxVisibility = Double.MIN_VALUE;
        minVisibility = Double.MAX_VALUE;
        avgVisibility = 0;

        avgWind = 0;
        avgHumidity = 0;
        avgFeelsLike = 0;

        // for figuring out the most common weather icon to be used for display
        Map<String, Integer> iconFreqs = new HashMap<String, Integer>();
        int maxFreq = 0;

        // for calculating avg
        double numHours = 1.0 / eventHourlyWeather.size();

        // iterate through the hourly information that has been extracted
        for (DataPoint dp : eventHourlyWeather) {
            // a mix of max, min, and avg values of weather information is now calculated
            maxTemp = Math.max(maxTemp, dp.getTemperature());
            minTemp = Math.min(minTemp, dp.getTemperature());

            maxCloudCover = Math.max(maxCloudCover, dp.getCloudCover());
            minCloudCover = Math.min(minCloudCover, dp.getCloudCover());

            maxVisibility = Math.max(maxVisibility, dp.getVisibility());
            minVisibility = Math.min(minVisibility, dp.getVisibility());

            avgTemp += numHours * dp.getTemperature();
            avgCloudCover += numHours * dp.getCloudCover();
            avgVisibility += numHours * dp.getVisibility();
            avgFeelsLike += numHours * dp.getApparentTemperature();
            avgWind += numHours * dp.getWindSpeed();
            avgHumidity += numHours * dp.getHumidity();

            if (iconFreqs.containsKey(dp.getIcon())) {
                iconFreqs.put(dp.getIcon(), iconFreqs.get(dp.getIcon()) + 1);
            } else {
                iconFreqs.put(dp.getIcon(), 1);
            }

            // updates the most common weather icon encountered in the hourly
            // weather information used so far
            if (maxFreq < iconFreqs.get(dp.getIcon())) {
                maxFreq = iconFreqs.get(dp.getIcon());
                freqIcon = dp.getIcon();
            }
        }
    }

    /**
     * Prepare query for getting weather information for a given day. Unix time
     * is the start time for an event
     */
    private void getDayForecast(long unixTime) {
        // NOTE that 48 hours worth of data is returned startin from unixTime
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
     * Prepare query to get current weather information
     */
    public void getCurrentlyForecast() {
        // current weather information does not include timestamp information in URL
        try {
            String weatherAPICall = weatherAPIUrl + weatherAPIKey
                    + "/" + searchLatitude + "," + searchLongitude;

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
            // passed end time of event - can stop getting the hourly information
            if (datapoint.getDate().compareTo(eventEndTime) > 0) {
                break;
            }
            // check if the hourly data point is relevant to the event
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

    /**
     * Extracts all the weather specific information from the response
     */
    private DataPoint createDataPoint(JSONObject data) {
        // all the information made available by Dark Sky API
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

        // create a new DataPoint object encapsulating all the information extracted form above
        return new DataPoint(apparentTemperature, apparentTemperatureMax,
                apparentTemperatureMaxTime, apparentTemperatureMin,
                apparentTemperatureMinTime, cloudCover, dewPoint, humidity,
                icon, precipProbability, precipType, summary, temperature,
                temperatureMax, temperatureMaxTime, temperatureMin,
                temperatureMinTime, time, visibility, windBearing, windSpeed);
    }

    /**
     * A DataBlock holds a list of data points, summary, and icon for the
     * response that was returned for a specific query
     */
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

        // create new DataBlock object with information extracted from above
        return new DataBlock(dataList, icon, summary);
    }

    /**
     * Extract information for each alert given appropriate part of the JSON
     * structure
     */
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
        if (severity != null) {
            severity = severity.substring(0, 1).toUpperCase() + severity.substring(1);
        }

        // create new Alert object to store parsed information from above
        return new Alert((description != null) ? description.substring(0, Math.min(description.length(), 100)) : null, expires, regions, severity, time, title,
                uri);
    }

    /**
     * Parses information for each alert based on the response
     */
    private List<Alert> createAlerts(JSONArray data) {
        List<Alert> alerts = new ArrayList<>();
        for (int x = 0; x < data.length(); x++) {
            alerts.add(createAlert(data.getJSONObject(x)));
        }
        return alerts;
    }

    /**
     * Create a new Response object using appropriate keys used in JSON data
     */
    private Response createResponse(JSONObject data) {
        // can extract directly based on key
        Double latitude = data.isNull("latitude")
                ? null : data.getDouble("latitude");
        Double longitude = data.isNull("longitude")
                ? null : data.getDouble("longitude");
        String timezone = data.isNull("timezone")
                ? null : data.getString("timezone");

        // need to parse the structures within the JSON data for this data
        DataPoint currently = data.isNull(CURRENT)
                ? null : createDataPoint(data.getJSONObject(CURRENT));
        DataBlock minutely = data.isNull(MINUTE)
                ? null : createDataBlock(data.getJSONObject(MINUTE));
        DataBlock hourly = data.isNull(HOUR)
                ? null : createDataBlock(data.getJSONObject(HOUR));
        DataBlock daily = data.isNull(DAY)
                ? null : createDataBlock(data.getJSONObject(DAY));
        List<Alert> alerts = data.isNull(ALERT)
                ? new ArrayList<>() : createAlerts(data.getJSONArray(ALERT));

        // create new response from extracted information
        return new Response(latitude, longitude, timezone, currently,
                minutely, hourly, daily, alerts);
    }

    /*
    ===============================================================
    Getters and setters
    ===============================================================
     */
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void clearSearchFields() {
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public double getMaxCloudCover() {
        return maxCloudCover;
    }

    public void setMaxCloudCover(double maxCloudCover) {
        this.maxCloudCover = maxCloudCover;
    }

    public double getMinCloudCover() {
        return minCloudCover;
    }

    public void setMinCloudCover(double minCloudCover) {
        this.minCloudCover = minCloudCover;
    }

    public double getAvgCloudCover() {
        return avgCloudCover;
    }

    public void setAvgCloudCover(double avgCloudCover) {
        this.avgCloudCover = avgCloudCover;
    }

    public double getMaxVisibility() {
        return maxVisibility;
    }

    public void setMaxVisibility(double maxVisibility) {
        this.maxVisibility = maxVisibility;
    }

    public double getMinVisibility() {
        return minVisibility;
    }

    public void setMinVisibility(double minVisibility) {
        this.minVisibility = minVisibility;
    }

    public double getAvgVisibility() {
        return avgVisibility;
    }

    public void setAvgVisibility(double avgVisibility) {
        this.avgVisibility = avgVisibility;
    }

    public double getAvgWind() {
        return avgWind;
    }

    public void setAvgWind(double avgWind) {
        this.avgWind = avgWind;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public double getAvgFeelsLike() {
        return avgFeelsLike;
    }

    public void setAvgFeelsLike(double avgPrecipChance) {
        this.avgFeelsLike = avgPrecipChance;
    }

    public String getFreqIcon() {
        return freqIcon;
    }

    public void setFreqIcon(String freqIcon) {
        this.freqIcon = freqIcon;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public FacesMessage getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(FacesMessage resultMsg) {
        this.resultMsg = resultMsg;
    }

}
