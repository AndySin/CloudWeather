/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Data;

import java.util.List;

/**
 * Contains an alert issued by a government authority.
 *
 * @author Andy
 */
public class Alert {

    // alert information provided by API
    private final String description;
    private final Long expires;
    private final List<String> regions;
    private final String severity;
    private final Long time;
    private final String title;
    private final String uri;

    /**
     * Storing information about a new alert
     */
    public Alert(String description, Long expires, List<String> regions,
            String severity, Long time, String title, String uri) {
        this.description = description;
        this.expires = expires;
        this.regions = regions;
        this.severity = severity;
        this.time = time;
        this.title = title;
        this.uri = uri;
    }
    
    /**
     * Getter and setter methods
     */
    
    public String getDescription() {
        return description;
    }

    public Long getExpires() {
        return expires;
    }

    public List<String> getRegions() {
        return regions;
    }

    public String getSeverity() {
        return severity;
    }

    public Long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }
}
