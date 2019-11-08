package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.AirQuality;

import java.util.List;

public class AirQualityRequest {
    //AirQuality
    private List<AirQuality> getAirQuality;

    public List<AirQuality> getGetAirQuality() {
        return getAirQuality;
    }

    public void setGetAirQuality(List<AirQuality> getAirQuality) {
        this.getAirQuality = getAirQuality;
    }
}
