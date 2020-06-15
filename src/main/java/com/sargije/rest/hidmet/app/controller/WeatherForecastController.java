package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.repository.CityRepository;
import com.sargije.rest.hidmet.app.services.HidmetDataService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
		@ApiResponse(code = 200, message = "Successfully retrieved list"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
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
	public CurrentForecast showCurrentForecastByCityName (@PathVariable Long cityId){
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
