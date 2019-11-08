package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.AirQuality;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface AirQualityRepository extends CrudRepository<AirQuality, Long> {
    boolean existsByActiveAndTableTime(boolean one, LocalDateTime tableTime);

    @Modifying
    @Query("update AirQuality a set a.active = 0 where a.active = 1")
    void updateAirQualitySetActiveToFalse();

    @Cacheable("airQuality")
    List<AirQuality> findByActive(boolean one);
}
