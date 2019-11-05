package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.City;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CityRepository extends CrudRepository<City, Long>{
	@Cacheable("currentForecastsCities")
	List<City> findDistinctByCurrentForecastsNotNull();
	@Cacheable("fivedayForecastsCities")
	List<City> findDistinctByFivedayForecastsNotNull();
	@Cacheable("shortTermForecastsCities")
	List<City> findDistinctByShortTermForecastsNotNull();
}
