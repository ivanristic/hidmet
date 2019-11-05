package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.Authorities;
import com.sargije.rest.hidmet.app.model.AuthoritiesId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuthoritiesRepository extends CrudRepository<Authorities, AuthoritiesId> {
}
