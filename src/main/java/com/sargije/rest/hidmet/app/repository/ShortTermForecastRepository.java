package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.ShortTermForecast;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface ShortTermForecastRepository extends CrudRepository<ShortTermForecast, Long>{

	@Cacheable("shortTermActiveForecasts")
	List<ShortTermForecast> findByActive(BigInteger active);
	boolean existsByActiveAndTableTime(BigInteger active, LocalDateTime tableTime);

	@Modifying
	@Query("update ShortTermForecast cf set cf.active = 0 where cf.active = 1")
	void updateShortTermForecastActiveToFalse();
}
