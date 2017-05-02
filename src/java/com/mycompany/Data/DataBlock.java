/*
 * Created by Andy Sin on 2017.03.19  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.Data;

import java.util.List;

/**
 * Represents the weather pattern over a period of time.
 *
 * @author Andy
 */
public class DataBlock {

    // Inforation available in JSON structure
    private final List<DataPoint> data;
    private final String icon;
    private final String summary;

    /**
     * Creates a new DataBlock object with appropriate information
     */
    public DataBlock(List<DataPoint> data, String icon, String summary) {
        this.data = data;
        this.icon = icon;
        this.summary = summary;
    }

    /**
     * Getter and setter methods
     */
    
    public List<DataPoint> getData() {
        return data;
    }

    public String getIcon() {
        return icon;
    }

    public String getSummary() {
        return summary;
    }
}
