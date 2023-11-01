package com.spring3.firstproject.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring3.firstproject.model.User;
import com.spring3.firstproject.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name " + username + "!");
        User user = repository.findByUserName(username);

        if (user != null) return user;
        throw new UsernameNotFoundException("Username: " + username + " not found!");
    }
}
