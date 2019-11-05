package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.CurrentForecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


@Transactional
public interface CurrentForecastRepository extends CrudRepository<CurrentForecast, Long>{

	@Cacheable("currentActiveForecasts")
	List<CurrentForecast> findByActive(BigInteger active);
	boolean existsByActiveAndTableTime(BigInteger active, LocalDateTime tableTime);

	@Modifying
	@Query("update CurrentForecast cf set cf.active = 0 where cf.active = 1")
	void updateCurrentForecastActiveToFalse();


}
