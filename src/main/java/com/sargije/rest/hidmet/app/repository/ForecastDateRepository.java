package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.ForecastDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface ForecastDateRepository extends CrudRepository<ForecastDate, Long>{
	ForecastDate findByForecastDate(LocalDate dateForecast);
	Page<ForecastDate> findByForecastDate(LocalDate date, Pageable pageable);
	List<ForecastDate> findByForecastDateAfter(LocalDate date);

}
