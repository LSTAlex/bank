package com.example.bank.Controllers;

import com.example.bank.Service.MyUserService;
import com.example.bank.model.UsersModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    private final MyUserService myUserService;

    public RegisterController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UsersModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UsersModel usersModel, RedirectAttributes redirectAttributes) {
       boolean isRegistered = myUserService.registerUser(usersModel);

       if (!isRegistered) {
           redirectAttributes.addFlashAttribute("errorMessage",
                   "Пользователь с таким именем существует!");
           return "redirect:/register";
       }else {
           redirectAttributes.addFlashAttribute("successMessage",
                   "Пользователь успешно зарегистрирован!");
           return "redirect:/login";
       }
    }
}