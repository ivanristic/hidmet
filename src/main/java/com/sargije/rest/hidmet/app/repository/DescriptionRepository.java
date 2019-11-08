package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.Description;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DescriptionRepository extends CrudRepository<Description, Long>{

	@Cacheable("description")
	Description findByImageLocation(String imageLocation);

	int countByDescriptionNotNull();
}
