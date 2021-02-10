package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.City;
import com.sargije.rest.hidmet.app.services.HidmetDataService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "406", description = "Input parameters are not as expected")
})
public class CityController {

    @Autowired
    HidmetDataService hidmetDataService;
    @GetMapping(value="/city/forecastType/{forecastType}")
    public List<City> showCityByForecastType(@PathVariable String forecastType){
        List<City> cityList = null;
        switch (forecastType) {
            case "current":
                cityList = hidmetDataService.getCityForCurrentForecasts();
                break;
            case "fiveDay":
                cityList = hidmetDataService.getCityForFivedayForecast();
                break;
            case "shortTerm":
                cityList = hidmetDataService.getCityForShortTermForecast();
                break;
            default:
                throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE, "Input parameters are not as expected"
                );
        }
        return cityList;
    }

    @GetMapping(value = "/city/{id}")
    public Optional<City> showCityById(@PathVariable Long id) {
        return  hidmetDataService.getCityById(id);
    }

    /*
    @GetMapping(value = "/city/current")
    public List<City> showCityForCurrentForecasts(){

        return hidmetDataService.getCityForCurrentForecasts();

    }



    @GetMapping(value = "/city/fiveday")
    public List<City> showCityForFivedayForecast(){

        return hidmetDataService.getCityForFivedayForecast();

    }

    @GetMapping(value = "/city/shortterm")
    public List<City> showCityForShortTermForecast(){

        return hidmetDataService.getCityForShortTermForecast();
    }

    */

}

