package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/readable")
public class ReadableController {
	@Autowired
	RestTemplate restTemplate;

	@Value("${hidmet.restapi.url}")
	String restApiUrl;

	@GetMapping(value = "/fiveday")
	public String getFiveDayForecast(Model model){

		ResponseEntity<FivedayForecast[]> fivedayForecast = restTemplate.getForEntity(restApiUrl + "/api/v1/fiveday", FivedayForecast[].class);
		//ResponseEntity<City[]> city = restTemplate.getForEntity(restApiUrl + "/rest/city/fiveday", City[].class);
		//using exchange
		//HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		//HttpEntity<City[]> citiesEntity = new HttpEntity<City[]>(headers);
		//City[] city = restTemplate.exchange(restApiUrl + "/api/v1/city/forecastType/fiveDay", HttpMethod.GET, citiesEntity, City[].class).getBody();
		ResponseEntity<City[]> city = restTemplate.getForEntity(restApiUrl + "/api/v1/city/forecastType/fiveDay", City[].class);
		model.addAttribute("listFiveDayForecast", fivedayForecast.getBody());
		model.addAttribute("cityList", city.getBody());
        return "fiveday";
	}

	@GetMapping(value = "/current")
	public String getCurrentForecast(Model model){

		ResponseEntity<CurrentForecast[]> currentForecast = restTemplate.getForEntity(restApiUrl + "/api/v1/current", CurrentForecast[].class);
		ResponseEntity<City[]> city = restTemplate.getForEntity(restApiUrl + "/api/v1/city/forecastType/current", City[].class);
		model.addAttribute("listCurrentForecast", currentForecast.getBody());
		model.addAttribute("cityList", city.getBody());
		return "current";
	}

	@GetMapping(value = "/shorterm")
	public String getShorttermForecast(Model model){

		ResponseEntity<ShortTermForecast[]> shortTermForecast = restTemplate.getForEntity(restApiUrl + "/api/v1/shortterm", ShortTermForecast[].class);
		ResponseEntity<City[]> city = restTemplate.getForEntity(restApiUrl + "/api/v1/city/forecastType/shortTerm", City[].class);
		model.addAttribute("listShortTermForecast", shortTermForecast.getBody());
		model.addAttribute("cityList", city.getBody());
		return "shortterm";
	}

	@GetMapping(value = "/airquality")
	public String getAirQuality(Model model){

		ResponseEntity<AirQuality[]> airQuality = restTemplate.getForEntity(restApiUrl + "/api/v1/airquality", AirQuality[].class);
		ResponseEntity<Station[]> stations = restTemplate.getForEntity(restApiUrl + "/api/v1/station/airquality", Station[].class);
		model.addAttribute("listAirQuality", airQuality.getBody());
		model.addAttribute("stationList", stations.getBody());
		return "airquality";
	}
	@GetMapping(value = "/user")
	public String getUserRegistrationPage(Model model){
		model.addAttribute("user", new User());
		//model.addAttribute("authority", new Authorities());
		return "form";
	}

	@PostMapping(value = "/user")
	public String createUserFromForm(@ModelAttribute User user, Model model){
		/*
		if(SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getUsername())){
			return null;
		}
		*/
		ResponseEntity<User> users = restTemplate.postForEntity(restApiUrl + "/api/v1/user", user, User.class);
		if(users.getStatusCode().equals(HttpStatus.OK)){
			model.addAttribute("status", "User has been created successfully");
		} else {
			model.addAttribute("status", "Failed user creation");
		}
		return "users";
	}
}
