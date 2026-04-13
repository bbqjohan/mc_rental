package com.sjoqvist.wigell_mc_rental.service;

import com.sjoqvist.wigell_mc_rental.repository.AppUserRepo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final AppUserRepo appUserRepo;

    public UserServiceImpl(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    @Transactional
    public boolean hasAccess(Long id) {
        var username = getUsername();
        var user =
                appUserRepo
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));

        return user.getCustomer().getId().equals(id);
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
