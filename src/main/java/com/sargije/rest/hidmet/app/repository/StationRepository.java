package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.Station;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StationRepository extends CrudRepository<Station, Long> {

    @Cacheable("git a")
    List<Station> findAll();

}
