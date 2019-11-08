package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.Station;

import java.util.List;

public class StationRequest {
    //Stations
    private List<Station> getStations;

    public List<Station> getGetStations() {
        return getStations;
    }

    public void setGetStations(List<Station> getStations) {
        this.getStations = getStations;
    }
}
