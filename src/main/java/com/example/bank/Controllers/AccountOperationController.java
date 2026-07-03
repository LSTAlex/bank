package com.example.bank.Controllers;

import com.example.bank.dto.AccountOperationDto;
import com.example.bank.dto.TransferDto;
import com.example.bank.model.UsersPrincipalModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.bank.Service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountOperationController {
    private final AccountService accountService;

    public AccountOperationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/deposit")
    public String depositAccount(@ModelAttribute AccountOperationDto dto,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try {
            accountService.depositToDebit(dto.getAccountId(),userId,dto.getAmount());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawdebit")
    public String withdrawDebit(@ModelAttribute AccountOperationDto dto,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromDebit(dto.getAccountId(),userId,dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/depositcredit")
    public String depositCredit(@ModelAttribute AccountOperationDto dto,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.depositToCredit(dto.getAccountId(), userId,dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawcredit")
    public String withdrawCredit(@ModelAttribute AccountOperationDto dto,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromCredit(dto.getAccountId(), userId,dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/depositsavings")
    public String depositSavings(@ModelAttribute AccountOperationDto dto,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.depositToSavings(dto.getAccountId(), userId,dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawsavings")
    public String withdrawSavings(@ModelAttribute AccountOperationDto dto,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromSavings(dto.getAccountId(), userId,dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/transfer")
    public String transferAccount(@ModelAttribute TransferDto dto,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();
        try{
           accountService.transferFromAccount(dto.getFromType(), dto.getFromId(),
                   dto.getOnType(), dto.getOnId(), userId, dto.getAmount());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }
}
