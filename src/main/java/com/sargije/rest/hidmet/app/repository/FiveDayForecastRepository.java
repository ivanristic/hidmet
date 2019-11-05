package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.FivedayForecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface FiveDayForecastRepository extends CrudRepository<FivedayForecast, Long>{

	@Cacheable("fivedayActiveForecasts")
	List<FivedayForecast> findByActive(BigInteger active);
	boolean existsByActiveAndTableTime(BigInteger active, LocalDateTime tableTime);


	@Modifying
	@Query("update FivedayForecast cf set cf.active = 0 where cf.active = 1")
	void updateFivedayForecastActiveToFalse();
}
