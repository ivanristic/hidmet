package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.ShortTermForecast;

import java.util.List;

public class ShortTermForecastRequest {

    //ShortTermForecast
    private List<ShortTermForecast> getShortTermForecast;

    public List<ShortTermForecast> getGetShortTermForecast() {
        return getShortTermForecast;
    }

    public void setGetShortTermForecast(List<ShortTermForecast> getShortTermForecast) {
        this.getShortTermForecast = getShortTermForecast;
    }
}
