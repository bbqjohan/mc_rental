package com.sjoqvist.wigell_mc_rental.service;

public interface UserService {
    boolean hasAccess(Long id);

    String getUsername();

    boolean isAdmin();
}
