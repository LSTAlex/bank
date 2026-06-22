package com.example.bank.Controllers;

import com.example.bank.model.UsersPrincipalModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.bank.Service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class AccountOperationController {
    private final AccountService accountService;

    public AccountOperationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/deposit")
    public String depositAccount(@RequestParam int accountId,
                                 @RequestParam BigDecimal amount,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try {
            accountService.depositToDebit(accountId,userId,amount);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawdebit")
    public String withdrawDebit(@RequestParam int accountId,
                                  @RequestParam BigDecimal amount,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromDebit(accountId,userId,amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/depositcredit")
    public String depositCredit(@RequestParam int accountId,
                                @RequestParam BigDecimal amount,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.depositToCredit(accountId,userId,amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawcredit")
    public String withdrawCredit(@RequestParam int accountId,
                                @RequestParam BigDecimal amount,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromCredit(accountId,userId,amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/depositsavings")
    public String depositSavings(@RequestParam int accountId,
                                @RequestParam BigDecimal amount,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.depositToSavings(accountId,userId,amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/withdrawsavings")
    public String withdrawSavings(@RequestParam int accountId,
                                 @RequestParam BigDecimal amount,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        try{
            accountService.withdrawFromSavings(accountId,userId,amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }

    @PostMapping("/account/transfer")
    public String transferAccount(@RequestParam String fromAccountId,
                                  @RequestParam String onAccountId,
                                  @RequestParam BigDecimal amount,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes){
        UsersPrincipalModel principal = (UsersPrincipalModel) authentication.getPrincipal();
        int userId = principal.getUsersModel().getId();

        String[] fromParts = fromAccountId.split("_");
        String fromType = fromParts[0];
        int fromId = Integer.parseInt(fromParts[1]);

        String[] onParts = onAccountId.split("_");
        String onType = onParts[0];
        int onId = Integer.parseInt(onParts[1]);

        try{
           accountService.transferFromAccount(fromType, fromId, onType, onId, userId, amount);
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/homeuser";
    }
}
