package com.me.clouddrive.controller;

import com.me.clouddrive.controller.payload.CreateUserPayload;
import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") CreateUserPayload createUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        userService.register(createUser.username(), createUser.email(), createUser.password());
        return "redirect:/cloud-drive";
    }
}
