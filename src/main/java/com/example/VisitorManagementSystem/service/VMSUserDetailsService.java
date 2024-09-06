package com.example.VisitorManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.VisitorManagementSystem.entity.User;
import com.example.VisitorManagementSystem.repo.UserRepo;

@Service
public class VMSUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepo.findByEmail(username);
        if(appUser == null){
            throw new UsernameNotFoundException("User does not exist");
        }
        return appUser;
    }
}
