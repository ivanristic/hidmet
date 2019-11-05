package com.sargije.rest.hidmet.app.services;

import com.sargije.rest.hidmet.app.model.Authorities;
import com.sargije.rest.hidmet.app.model.AuthoritiesId;
import com.sargije.rest.hidmet.app.model.Users;
import com.sargije.rest.hidmet.app.repository.AuthoritiesRepository;
import com.sargije.rest.hidmet.app.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UsersDataService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    public void createUser(Users user){
        usersRepository.save(user);
    }

    public Users getUserByUsername(String username){ return usersRepository.findUsersByUsername(username); }

    public void createAuthorities(Set<Authorities> authority){
        authoritiesRepository.saveAll(authority);
    }

    public boolean existAuthority(AuthoritiesId autId ){ return authoritiesRepository.existsById(autId); }

    public void deleteAuthority(AuthoritiesId autId){ authoritiesRepository.deleteById(autId); }

}
