package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.entity.AppUser;
import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepo appUserRepo;

    public AppUserDetailsService(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser =
                appUserRepo
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));

        var authorities =
                appUser.getRoles().stream()
                        .map((r) -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                        .collect(Collectors.toSet());

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
