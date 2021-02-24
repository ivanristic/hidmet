package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.model.Authorities;
import com.sargije.rest.hidmet.app.model.AuthoritiesId;
import com.sargije.rest.hidmet.app.model.User;
import com.sargije.rest.hidmet.app.model.Users;
import com.sargije.rest.hidmet.app.services.UsersDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    UsersDataService usersDataService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(value = "/user")
    public void registerUser(@RequestBody User user){

        Users users = new Users(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.isEnabled());
        usersDataService.createUser(users);

        Set<Authorities> authorities = new HashSet<Authorities>();
        if(user.isAdmin()){
            AuthoritiesId autId = new AuthoritiesId();
            Authorities authority = new Authorities();
            autId.setAuthority("admin");
            autId.setUsername(user.getUsername());
            authority.setId(autId);
            //authority.setUsers(users);
            authorities.add(authority);
        }else{
            AuthoritiesId autId = new AuthoritiesId();
            autId.setAuthority("admin");
            autId.setUsername(user.getUsername());
            if(usersDataService.existAuthority(autId)) {
                usersDataService.deleteAuthority(autId);//authoritiesRepository.deleteById(autId);
            }
        }
        if(user.isUser()){
            AuthoritiesId autId = new AuthoritiesId();
            Authorities authority = new Authorities();
            autId.setAuthority("user");
            autId.setUsername(user.getUsername());
            authority.setId(autId);
           // authority.setUsers(users);
            authorities.add(authority);
        }

        usersDataService.createAuthorities(authorities);


    }
    @GetMapping(value = "/user/{username}")
    public Users getUserByUsername(@PathVariable String username){
        return usersDataService.getUserByUsername(username);
    }

}
