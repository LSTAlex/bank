package com.example.bank.mapper;

import com.example.bank.dto.UserRegistrationDto;
import com.example.bank.model.UsersModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "debitUsers", ignore = true)
    @Mapping(target = "creditAccounts", ignore = true)
    @Mapping(target = "savingsAccountModels", ignore = true)
    UsersModel toEntity(UserRegistrationDto dto);
}
