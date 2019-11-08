package com.sargije.rest.hidmet.app.graphql.service.requests;

import com.sargije.rest.hidmet.app.model.City;
import io.aexp.nodes.graphql.annotations.GraphQLArgument;

import java.util.List;

public class CityRequest {
    // City
    @GraphQLArgument(name="forecastType")
    private List<City> getAllCities;

    public List<City> getGetAllCities() {
        return getAllCities;
    }

    public void setGetAllCities(List<City> getAllCities) {
        this.getAllCities = getAllCities;
    }
}
