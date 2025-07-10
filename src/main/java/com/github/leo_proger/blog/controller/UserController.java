package com.github.leo_proger.blog.controller;

import com.github.leo_proger.blog.dto.UserDTO;
import com.github.leo_proger.blog.exception.UserAlreadyExistsException;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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
    public String signupPost(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "signup";
        }

        try {
            User user = userService.save(userDTO);
            Authentication result = userService.authenticate(user.getUsername(), userDTO.getPassword(), user.getAuthorities());

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(result);

            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("username", "error.username", e.getMessage());
            model.addAttribute("user", userDTO);
            return "signup";
        }
        return "redirect:/";
    }
}
