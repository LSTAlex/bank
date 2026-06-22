package com.example.bank.Controllers;

import com.example.bank.model.UsersModel;
import com.example.bank.model.UsersPrincipalModel;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import com.example.bank.Service.AccountService;
import com.example.bank.model.ScoreTypeModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class ScoreController {

    private final AccountService accountService;

    public ScoreController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/scoreadd")
    public String scoreAdd(Model model) {
        model.addAttribute("ScoreType", ScoreTypeModel.values());
        return "scoreadd";
    }

    @PostMapping("/scoreadd")
    public String scoreAdd(@RequestParam ScoreTypeModel scoreTypeModel,
                           @RequestParam(required = false) BigDecimal creditLimit,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        UsersPrincipalModel usersPrincipalModel = (UsersPrincipalModel) authentication.getPrincipal();
        UsersModel user = usersPrincipalModel.getUsersModel();

        try{
            switch (scoreTypeModel) {
                case DEBIT:
                    accountService.createDebitUsersScore(user);
                    redirectAttributes.addFlashAttribute("successMessage", "Дебетовый счет успешно открыт!");
                    break;
                case CREDIT:
                    if (creditLimit == null || creditLimit.compareTo(BigDecimal.ZERO) > 0
                            || creditLimit.compareTo(new BigDecimal("-100000")) < 0) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Укажите корректный кредитный лимит!");
                        return "redirect:/scoreadd";
                    }
                    accountService.createCreditUsersScore(user, creditLimit);
                    redirectAttributes.addFlashAttribute("successMessage", "Кредитный счет успешно открыт!");
                    break;
                case SAVINGS:
                    accountService.createSavingsUsersScore(user);
                    redirectAttributes.addFlashAttribute("successMessage", "Накопительный счет успешно открыт!");
                    break;
            }
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/scoreadd";
    }
}
