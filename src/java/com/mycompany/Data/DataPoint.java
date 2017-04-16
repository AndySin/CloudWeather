/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Data;

/**
 * Represents the average weather pattern over a specific period of time.
 *
 * @author Andy
 */
public class DataPoint {
    private String timeFormatted;
    private final Double apparentTemperature;
    private final Double apparentTemperatureMax;
    private final Long apparentTemperatureMaxTime;
    private final Double apparentTemperatureMin;
    private final Long apparentTemperatureMinTime;
    private final Double cloudCover;
    private final Double dewPoint;
    private final Double humidity;
    private final String icon;
    private final Double precipProbability;
    private final String precipType;
    private final String summary;
    private final Double temperature;
    private final Double temperatureMax;
    private final Long temperatureMaxTime;
    private final Double temperatureMin;
    private final Long temperatureMinTime;
    private final Long time;
    private final Double visibility;
    private final String windBearing;
    private final Double windSpeed;

    public DataPoint(Double apparentTemperature, Double apparentTemperatureMax,
            Long apparentTemperatureMaxTime, Double apparentTemperatureMin,
            Long apparentTemperatureMinTime, Double cloudCover, Double dewPoint,
            Double humidity, String icon, Double precipProbability,
            String precipType, String summary, Double temperature,
            Double temperatureMax, Long temperatureMaxTime,
            Double temperatureMin, Long temperatureMinTime, Long time,
            Double visibility, Integer windBearing, Double windSpeed) {
        this.apparentTemperature = apparentTemperature;
        this.apparentTemperatureMax = apparentTemperatureMax;
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
        this.apparentTemperatureMin = apparentTemperatureMin;
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
        this.cloudCover = cloudCover;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.icon = icon;
        this.precipProbability = precipProbability;
        this.precipType = precipType;
        this.summary = summary;
        this.temperature = temperature;
        this.temperatureMax = temperatureMax;
        this.temperatureMaxTime = temperatureMaxTime;
        this.temperatureMin = temperatureMin;
        this.temperatureMinTime = temperatureMinTime;
        this.time = time;
        this.visibility = visibility;
        this.windBearing = windDirection(windBearing);
        this.windSpeed = windSpeed;
    }

    private String windDirection(Integer windBearing) {
        if (windBearing == null) {
            return null;
        }
        int wind = (int) windBearing;
        if (337.5 < wind || wind < 22.5) {
            return "north";
        } else if (wind < 67.5) {
            return "northeast";
        } else if (wind < 112.5) {
            return "east";
        } else if (wind < 157.5) {
            return "southeast";
        } else if (wind < 202.5) {
            return "south";
        } else if (wind < 247.5) {
            return "southwest";
        } else if (wind < 292.5) {
            return "west";
        } else {
            return "northwest";
        }
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public Double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public Long getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public Double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public Long getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public Double getCloudCover() {
        return cloudCover;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public Double getHumidity() {
        return humidity;
    }

    public String getIcon() {
        return icon;
    }

    public Double getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public String getSummary() {
        return summary;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public Long getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public Long getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public Long getTime() {
        return time;
    }

    public Double getVisibility() {
        return visibility;
    }

    public String getWindBearing() {
        return windBearing;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getTimeFormatted() {
        return timeFormatted;
    }

    public void setTimeFormatted(String timeFormatted) {
        this.timeFormatted = timeFormatted;
    }
    
}
