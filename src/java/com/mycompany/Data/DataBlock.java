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

    private final List<DataPoint> data;
    private final String summary;
    private final String icon;

    public DataBlock(List<DataPoint> data, String summary, String icon) {
        this.data = data;
        this.summary = summary;
        this.icon = icon;
    }

    public List<DataPoint> getData() {
        return data;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }
}
