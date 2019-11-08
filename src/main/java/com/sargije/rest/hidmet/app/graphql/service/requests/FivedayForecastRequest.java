package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.FivedayForecast;

import java.util.List;

public class FivedayForecastRequest {

    //FivedayForecast
    private List<FivedayForecast> getFivedayForecast;

    public List<FivedayForecast> getGetFivedayForecast() {
        return getFivedayForecast;
    }

    public void setGetFivedayForecast(List<FivedayForecast> getFivedayForecast) {
        this.getFivedayForecast = getFivedayForecast;
    }
}
