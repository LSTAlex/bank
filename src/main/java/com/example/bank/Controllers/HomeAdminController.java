package com.example.bank.Controllers;

import com.example.bank.Service.AccountService;
import com.example.bank.Service.TimeService;
import com.example.bank.dto.AdminAccountViewDto;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.model.CreditUsersModel;
import com.example.bank.model.DebitUsersModel;
import com.example.bank.model.SavingsUsersModel;
import com.example.bank.repository.CreditUsersRepository;
import com.example.bank.repository.DebitUsersRepository;
import com.example.bank.repository.SavingsUserRpository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeAdminController {

    private final TimeService timeService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public HomeAdminController(DebitUsersRepository debitUsersRepository, CreditUsersRepository creditUsersRepository, SavingsUserRpository savingsUserRpository, TimeService timeService, AccountService accountService, AccountMapper accountMapper) {

        this.timeService = timeService;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }


    @GetMapping("/homeadmin")
    public String homeAdmin(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") String type, Model model) {
        accountService.pepositorySwitch(page,type,model);
        return "homeadmin";
    }

    @PostMapping("/homeadmin/fastforward")
    public String fastForwardTime(@RequestParam int days,
                                  RedirectAttributes redirectAttributes) {
        timeService.forwardTime(days);
        accountService.processMaintenanceFees(); // Сразу применяем изменения
        redirectAttributes.addFlashAttribute("successMessage",
                "Время перемотано на " + days + " дней. Обслуживание выполнено.");
        return "redirect:/homeadmin";
    }
}
