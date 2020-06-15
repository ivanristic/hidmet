package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.City;
import com.sargije.rest.hidmet.app.model.FivedayForecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface FiveDayForecastRepository extends CrudRepository<FivedayForecast, Long>{

	@Cacheable("fivedayActiveForecasts")
	List<FivedayForecast> findByActive(boolean active);
	boolean existsByActiveAndTableTime(boolean active, LocalDateTime tableTime);


	@Modifying
	@Query("update FivedayForecast cf set cf.active = 0 where cf.active = 1")
	void updateFivedayForecastActiveToFalse();

    List<FivedayForecast> findShortTermForecastByCityAndActive(Optional<City> city, boolean active);
}
