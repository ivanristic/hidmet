package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.CurrentForecast;

import java.util.List;

public class CurrentForecastRequest {

    //CurrentForecast
    private List<CurrentForecast> getCurrentForecast;

    public List<CurrentForecast> getGetCurrentForecast() {
        return getCurrentForecast;
    }

    public void setGetCurrentForecast(List<CurrentForecast> getCurrentForecast) {
        this.getCurrentForecast = getCurrentForecast;
    }
}
