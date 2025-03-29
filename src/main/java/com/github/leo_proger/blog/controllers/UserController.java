package com.github.leo_proger.blog.controllers;

import com.github.leo_proger.blog.dto.UserDTO;
import com.github.leo_proger.blog.exceptions.UserAlreadyExistsException;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupGet(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "signup";
        }

        try {
            User user = userService.save(userDTO);
            userService.authenticateUser(user);
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("username", "error.username", e.getMessage());
            model.addAttribute("user", userDTO);
            return "signup";
        }
        return "redirect:/";
    }
}
