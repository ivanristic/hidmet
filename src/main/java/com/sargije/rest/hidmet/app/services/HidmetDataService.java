package com.sargije.rest.hidmet.app.services;

import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HidmetDataService {
	
	@Autowired
	CurrentForecastRepository currentForecastRepository;
	
	@Autowired
	ShortTermForecastRepository shortTermForecastRepository;
	
	@Autowired
	FiveDayForecastRepository fiveDayForecastRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	AirQualityRepository airQualityRepository;

	@Autowired
	StationRepository stationRepository;

	public List<CurrentForecast> getCurrentForecast(){
		return currentForecastRepository.findByActive(true);
	}

	public List<ShortTermForecast> getShortTermForecast(){
		return shortTermForecastRepository.findByActive(true);
	}

	public List<FivedayForecast> getFiveDayForecast(){
		return fiveDayForecastRepository.findByActive(true);
	}

	public List<City> getCityForCurrentForecasts() {
		return cityRepository.findDistinctByCurrentForecastsNotNull();
	}

	public List<City> getCityForFivedayForecast() {
		return cityRepository.findDistinctByFivedayForecastsNotNull();
	}

	public List<City> getCityForShortTermForecast() {
		return cityRepository.findDistinctByShortTermForecastsNotNull();
	}

	public List<City> getAllCities(){
		return (List<City>) cityRepository.findAll();
	}

	public List<AirQuality> getAirQuality() {
		return airQualityRepository.findByActive(true);
	}

	public List<Station> getStationsForAirQuality() { return (List<Station>) stationRepository.findAll();}

	public Optional<City> getCityById(Long id) { return cityRepository.findById(id);	}

    public CurrentForecast getCurrentForecastByCityAndActive(Optional<City> city, boolean active) {
		return currentForecastRepository.findCurrentForecastByCityAndActive(city, active);
    }

    public List<ShortTermForecast> getShortTermForecastByCityAndActive(Optional<City> city, boolean active) {
		return shortTermForecastRepository.findShortTermForecastByCityAndActive(city, active);
	}

	public List<FivedayForecast> getFivedayForecastByCityAndActive(Optional<City> city, boolean active) {
		return fiveDayForecastRepository.findShortTermForecastByCityAndActive(city, active);
	}
}
