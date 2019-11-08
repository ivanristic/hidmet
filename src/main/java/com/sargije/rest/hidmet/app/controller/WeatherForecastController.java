package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.services.HidmetDataService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved list"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
})
public class WeatherForecastController {
	
	@Autowired
	HidmetDataService hidmetDataService;
	
	@GetMapping(value = "/current")
	public List<CurrentForecast> showCurrentForecast(){

		return hidmetDataService.getCurrentForecast();

	}

	@GetMapping(value = "/shortterm")
	public List<ShortTermForecast> showShortTermForecast(){

		return hidmetDataService.getShortTermForecast();

	}
	
	@GetMapping(value = "/fiveday")
	public List<FivedayForecast> showFiveDayForecast(){
		
		return hidmetDataService.getFiveDayForecast();
	}

    @GetMapping(value="/airquality")
    public List<AirQuality> showAirQuality(){
	    return hidmetDataService.getAirQuality();
    }

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

	@GetMapping(value = "/station/airquality")
	public List<Station> showCityForAirQuality(){

		return hidmetDataService.getStationsForAirQuality();
	}

}
