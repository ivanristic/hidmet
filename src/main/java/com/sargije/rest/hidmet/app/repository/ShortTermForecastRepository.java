package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.City;
import com.sargije.rest.hidmet.app.model.ShortTermForecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface ShortTermForecastRepository extends CrudRepository<ShortTermForecast, Long>{

	@Cacheable("shortTermActiveForecasts")
	List<ShortTermForecast> findByActive(boolean active);
	boolean existsByActiveAndTableTime(boolean active, LocalDateTime tableTime);

	@Modifying
	@Query("update ShortTermForecast cf set cf.active = 0 where cf.active = 1")
	void updateShortTermForecastActiveToFalse();

    List<ShortTermForecast> findShortTermForecastByCityAndActive(Optional<City> city, boolean active);
}
