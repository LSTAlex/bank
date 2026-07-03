package com.example.bank.Service;

import com.example.bank.dto.UserRegistrationDto;
import com.example.bank.mapper.UserMapper;
import com.example.bank.model.UsersModel;
import com.example.bank.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private final UserMapper userMapper;

    public MyUserService(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public boolean registerUser(UserRegistrationDto dto) {

        if (usersRepository.findByUsername(dto.getUsername()) != null) return false;
        UsersModel user = userMapper.toEntity(dto);
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        try {
            usersRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
