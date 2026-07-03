package com.example.bank.Controllers;

import com.example.bank.dto.AccountOperationDto;
import com.example.bank.dto.TransferDto;
import com.example.bank.dto.UserAccountViewDto;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.model.CreditUsersModel;
import com.example.bank.model.DebitUsersModel;
import com.example.bank.model.SavingsUsersModel;
import com.example.bank.model.UsersPrincipalModel;
import com.example.bank.repository.CreditUsersRepository;
import com.example.bank.repository.DebitUsersRepository;
import com.example.bank.repository.SavingsUserRpository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeUserController {

    private final DebitUsersRepository debitUsersRepository;
    private final CreditUsersRepository creditUsersRepository;
    private final SavingsUserRpository savingsUserRpository;
    private final AccountMapper accountMapper;

    public HomeUserController(DebitUsersRepository debitUsersRepository,
                              CreditUsersRepository creditUsersRepository,
                              SavingsUserRpository savingsUserRpository, AccountMapper accountMapper) {

        this.debitUsersRepository = debitUsersRepository;
        this.creditUsersRepository = creditUsersRepository;
        this.savingsUserRpository = savingsUserRpository;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/homeuser")
    public String homeUser(Authentication authentication, Model model) {

        UsersPrincipalModel usersPrincipalModel = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = usersPrincipalModel.getUsersModel().getId();

        List<DebitUsersModel> debitAccount = debitUsersRepository.findByUserId(userId);
        debitAccount.sort(Comparator.comparing(DebitUsersModel::getId));

        List<CreditUsersModel> creditAccount = creditUsersRepository.findByUserId(userId);
        creditAccount.sort(Comparator.comparing(CreditUsersModel::getId));

        List<SavingsUsersModel> savingsAccount = savingsUserRpository.findByUserId(userId);
        savingsAccount.sort(Comparator.comparing(SavingsUsersModel::getId));

        List<UserAccountViewDto> debitDtos = debitAccount.stream().map(accountMapper::toViewDto).toList();
        List<UserAccountViewDto> creditDtos = creditAccount.stream().map(accountMapper::toViewDto).toList();
        List<UserAccountViewDto> savingsDtos = savingsAccount.stream().map(accountMapper::toViewDto).toList();

        // Передаем в HTML
        model.addAttribute("debitAccounts", debitDtos);
        model.addAttribute("creditAccounts", creditDtos);
        model.addAttribute("savingsAccounts", savingsDtos);
        model.addAttribute("transferDto", new TransferDto());

        return "homeuser";
    }
}
