/*
 * Created by Andy Sin on 2017.03.02  * 
 * Copyright © 2017 Andy Sin. All rights reserved. * 
 */
package WeatherSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.json.JSONArray;

/**
 *
 * @author Andy
 */
@SessionScoped

@Named(value = "searchedWeatherController")

public class SearchedWeatherController implements Serializable {

    // URL for weather API
    private final String weatherAPIUrl = "api.openweathermap.org/data/2.5/";

    // API Key for weather API
    private final String weatherAPIKey = "4c99a07ca200047bf938adedb4a7891e";

    /* @TODO */
    private String customizeAPICall = "weather";

    private String searchQuery;

    public String getForecast() {
        JSONArray jsonArray;

        try {
            String weatherAPICall = weatherAPIUrl + customizeAPICall
                    + searchQuery + "&appid=" + weatherAPIKey;
            String JSONData = readUrlContent(weatherAPICall);
            jsonArray = new JSONArray("[" + JSONData + "]");
            
            /* @TODO */
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * Return the content of a given URL as String
     *
     * @param url URL to retrieve the JSON data from
     * @return JSON data from the given URL as String
     * @throws Exception
     */
    private String readUrlContent(String url) throws Exception {
        BufferedReader urlReader = null;
        try {
            URL weatherUrl = new URL(url);
            urlReader = new BufferedReader(
                    new InputStreamReader(weatherUrl.openStream()));
            StringBuilder JSONResult = new StringBuilder();
            int current = urlReader.read();
            while(current != -1) {
                JSONResult.append((char)current);
                current = urlReader.read();
            }
            return JSONResult.toString();
        }
        finally {
            if(urlReader != null) {
                urlReader.close();
            }
        }
    }

}
