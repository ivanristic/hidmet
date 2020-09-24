package com.sargije.rest.hidmet.app.graphql.service.resolvers;

import com.sargije.rest.hidmet.app.model.Authorities;
import com.sargije.rest.hidmet.app.model.Users;
import com.sargije.rest.hidmet.app.services.UsersDataService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class Muitations implements GraphQLMutationResolver {

    @Autowired
    UsersDataService usersDataService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean createNewAuthority(Set<Authorities> authorities){

        usersDataService.createAuthorities(authorities);
        return true;
    }

    public boolean createNewUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersDataService.createUser(user);
        return true;
    }
}
