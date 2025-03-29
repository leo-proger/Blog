package com.github.leo_proger.blog.controllers;

import com.github.leo_proger.blog.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final PostService postService;

    public MainController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/", "/posts"})
    public String index(Model model) {
        model.addAttribute(
                "posts", postService.findAllByOrderByCreatedAtDesc()
        );
        return "main";
    }
}
