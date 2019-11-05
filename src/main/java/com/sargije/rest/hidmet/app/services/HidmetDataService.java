package com.sargije.rest.hidmet.app.services;

import com.sargije.rest.hidmet.app.model.*;
import com.sargije.rest.hidmet.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

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

	public List<CurrentForecast> getCurrentForecast(){
		return currentForecastRepository.findByActive(BigInteger.ONE);
	}

	public List<ShortTermForecast> getShortTermForecast(){
		return shortTermForecastRepository.findByActive(BigInteger.ONE);
	}

	public List<FivedayForecast> getFiveDayForecast(){
		return fiveDayForecastRepository.findByActive(BigInteger.ONE);
	}

	public List<City> getCityForCurrentForecastsRepository() {
		return cityRepository.findDistinctByCurrentForecastsNotNull();
	}

	public List<City> getCityForFivedayForecastRepository() {
		return cityRepository.findDistinctByFivedayForecastsNotNull();
	}

	public List<City> getCityForShortTermForecastRepository() {
		return cityRepository.findDistinctByShortTermForecastsNotNull();
	}

	public List<City> getAllCities(){
		return (List<City>) cityRepository.findAll();
	}

    public List<AirQuality> getAirQuality() {
        return airQualityRepository.findByActive(BigInteger.ONE);
    }
}
