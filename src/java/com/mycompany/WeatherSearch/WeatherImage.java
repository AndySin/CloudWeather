/*
 * Created by Andy Sin on 2017.03.11  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package com.mycompany.WeatherSearch;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andy
 */
public class WeatherImage {
    
    private static final WeatherImage weatherImage = new WeatherImage();
    
    private static final Map<String, String> weatherToImage = 
            new HashMap<String, String>();
    
    // TODO: Put stuff in the map once I find good images
    
    private WeatherImage() {
        
    }
    
    public static WeatherImage getInstance() {
        return weatherImage;
    }
    public static String getWeatherImage(String weather) {
        return weatherToImage.get(weather);
    }
}
