package com.sargije.rest.hidmet.app.repository;

import com.sargije.rest.hidmet.app.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findUsersByUsername(String username);
}
