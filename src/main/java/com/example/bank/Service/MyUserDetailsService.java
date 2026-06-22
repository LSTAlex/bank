package com.example.bank.Service;

import com.example.bank.model.UsersModel;

import com.example.bank.model.UsersPrincipalModel;
import com.example.bank.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsersModel userModel = usersRepository.findByUsername(username);
        if (userModel == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UsersPrincipalModel(userModel) ;
    }
}
