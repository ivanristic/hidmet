package com.sargije.rest.hidmet.app.graphql.service.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.services.HidmetDataService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Query implements GraphQLQueryResolver {

    @Autowired
    HidmetDataService hidmetDataService;

     public List<ShortTermForecast> getShortTermForecast(){
        return hidmetDataService.getShortTermForecast();//shortTermForecastRepository.findByActive(BigInteger.ONE);
    }

    public List<CurrentForecast> getCurrentForecast(){
        // RuntimeWiring.newRuntimeWiring().scalar(ExtendedScalars.DateTime);
        return hidmetDataService.getCurrentForecast();//currentForecastRepository.findByActive(BigInteger.ONE);
    }

    public List<FivedayForecast> getFivedayForecast(){
        // RuntimeWiring.newRuntimeWiring().scalar(ExtendedScalars.DateTime);
        return hidmetDataService.getFiveDayForecast();//fiveDayForecastRepository.findByActive(BigInteger.ONE);
    }

    public List<AirQuality> getAirQuality(){
         return hidmetDataService.getAirQuality();
    }

    public List<Station> getStations(){return hidmetDataService.getStationsForAirQuality();}

    public List<City> getAllCities(@NotNull ForecastType forecastType){

        List<City> cities = null;

        if (forecastType.equals(ForecastType.CURRENT)) {
            cities = hidmetDataService.getCityForCurrentForecasts();//cityRepository.findDistinctByCurrentForecastsNotNull();
        }else if(forecastType.equals(ForecastType.FIVEDAY)){
            cities = hidmetDataService.getCityForFivedayForecast();//cityRepository.findDistinctByFivedayForecastsNotNull();
        }else if(forecastType.equals(ForecastType.SHORT_TERM)){
            cities = hidmetDataService.getCityForShortTermForecast();//cityRepository.findDistinctByShortTermForecastsNotNull();
        }else if(forecastType.equals(ForecastType.ALL)) {
            cities = (List<City>) hidmetDataService.getAllCities();//cityRepository.findAll();
        }else {
            throw new IllegalStateException("Unexpected value: " + forecastType);
        }

        return cities;
    }

    enum ForecastType {
        CURRENT,
        FIVEDAY,
        SHORT_TERM,
        ALL
    }
}
