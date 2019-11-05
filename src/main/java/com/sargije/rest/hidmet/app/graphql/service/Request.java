package com.sargije.rest.hidmet.app.graphql.service;

import com.sargije.rest.hidmet.app.model.*;
import io.aexp.nodes.graphql.annotations.GraphQLArgument;

import java.util.List;

public class Request {

    // City
    @GraphQLArgument(name="forecastType")
    private List<City> getAllCities;

    public List<City> getGetAllCities() {
        return getAllCities;
    }

    public void setGetAllCities(List<City> getAllCities) {
        this.getAllCities = getAllCities;
    }

    //FivedayForecast
    private List<FivedayForecast> getFivedayForecast;

    public List<FivedayForecast> getGetFivedayForecast() {
        return getFivedayForecast;
    }

    public void setGetFivedayForecast(List<FivedayForecast> getFivedayForecast) {
        this.getFivedayForecast = getFivedayForecast;
    }

    //CurrentForecast
    private List<CurrentForecast> getCurrentForecast;

    public List<CurrentForecast> getGetCurrentForecast() {
        return getCurrentForecast;
    }

    public void setGetCurrentForecast(List<CurrentForecast> getCurrentForecast) {
        this.getCurrentForecast = getCurrentForecast;
    }

    //ShortTermForecast
    private List<ShortTermForecast> getShortTermForecast;

    public List<ShortTermForecast> getGetShortTermForecast() {
        return getShortTermForecast;
    }

    public void setGetShortTermForecast(List<ShortTermForecast> getShortTermForecast) {
        this.getShortTermForecast = getShortTermForecast;
    }

    //AirQuality
    private List<AirQuality> getAirQuality;

    public List<AirQuality> getGetAirQuality() {return getAirQuality; }

    public void setGetAirQuality(List<AirQuality> getAirQuality) {
        this.getAirQuality = getAirQuality;
    }
}
