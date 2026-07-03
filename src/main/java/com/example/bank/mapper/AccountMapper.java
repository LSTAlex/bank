package com.example.bank.mapper;

import com.example.bank.dto.AdminAccountViewDto;
import com.example.bank.dto.UserAccountViewDto;
import com.example.bank.model.CreditUsersModel;
import com.example.bank.model.DebitUsersModel;
import com.example.bank.model.SavingsUsersModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    //--------------View for users--------------
    @Mapping(target = "type", constant ="DEBIT")
    @Mapping(target = "creditLimit", ignore = true)
    @Mapping(target = "interestRate", ignore = true)
    UserAccountViewDto toViewDto(DebitUsersModel debitUsersModel);

    @Mapping(target = "type", constant = "CREDIT")
    @Mapping(target = "interestRate", ignore = true)
    UserAccountViewDto toViewDto(CreditUsersModel creditUsersModel);

    @Mapping(target = "type", constant = "SAVINGS")
    @Mapping(target = "creditLimit", ignore = true)
    UserAccountViewDto toViewDto(SavingsUsersModel savingsUsersModel);
    //------------------------------------------------

    //---------------View for Admins---------------
    @Mapping(target = "type", constant = "DEBIT")
    @Mapping(target = "creditLimit", ignore = true)
    @Mapping(target = "interestRate", ignore = true)
    @Mapping(source = "user.username", target = "username")
    AdminAccountViewDto toAdminViewDto(DebitUsersModel model);

    @Mapping(target = "type", constant = "CREDIT")
    @Mapping(target = "interestRate", ignore = true)
    @Mapping(source = "user.username", target = "username")
    AdminAccountViewDto toAdminViewDto(CreditUsersModel model);

    @Mapping(target = "type", constant = "SAVINGS")
    @Mapping(target = "creditLimit", ignore = true)
    @Mapping(source = "user.username", target = "username")
    AdminAccountViewDto toAdminViewDto(SavingsUsersModel model);
    //------------------------------------------------
}
