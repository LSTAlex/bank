package com.example.bank.Service;

import com.example.bank.model.UsersModel;
import com.example.bank.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public MyUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean registerUser(UsersModel usersModel) {

        if (usersRepository.findByUsername(usersModel.getUsername()) != null) {
            return false;
        }
        usersModel.setPassword(bCryptPasswordEncoder.encode(usersModel.getPassword()));
        usersModel.setRole("USER");

        try {
            usersRepository.save(usersModel);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
