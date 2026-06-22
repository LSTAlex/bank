package com.example.bank.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UsersPrincipalModel implements UserDetails{

    private  UsersModel usersModel;

    public UsersPrincipalModel(UsersModel usersModel) {
        this.usersModel = usersModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(usersModel.getRole()));
    }

    @Override
    public String getPassword() {
        return usersModel.getPassword();
    }

    @Override
    public String getUsername() {
        return usersModel.getUsername();
    }

    public UsersModel getUsersModel() {
        return usersModel;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
