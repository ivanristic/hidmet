package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.repository.CityRepository;
import com.sargije.rest.hidmet.app.services.HidmetDataService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class WeatherForecastController {
	
	@Autowired
	HidmetDataService hidmetDataService;

	@Autowired
	CityRepository cityRepository;
	
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


	@GetMapping(value = "/current/active/{cityId}")
	public CurrentForecast showCurrentForecastByCityName (
			@Parameter(name =  "cityId", example = "1", required = true)
			@PathVariable Long cityId
	){
		Optional<City> city = cityRepository.findById(cityId);

		return hidmetDataService.getCurrentForecastByCityAndActive(city, true);
	}

	@GetMapping(value = "/shortTerm/active/{cityId}")
	public List<ShortTermForecast> showShortTermForecastForecastByCityName (@PathVariable Long  cityId){
		Optional<City> city = cityRepository.findById(cityId);

		return hidmetDataService.getShortTermForecastByCityAndActive(city, true);
	}

	@GetMapping(value = "/fiveday/active/{cityId}")
	public List<FivedayForecast> showFivedayForecastForecastByCityName (@PathVariable Long  cityId){
		Optional<City> city = cityRepository.findById(cityId);

		return hidmetDataService.getFivedayForecastByCityAndActive(city, true);
	}

	@GetMapping(value = "/station/airquality")
	public List<Station> showCityForAirQuality(){

		return hidmetDataService.getStationsForAirQuality();
	}
}
