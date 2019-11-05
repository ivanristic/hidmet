package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StationRepository extends CrudRepository<Station, Long> {

}
