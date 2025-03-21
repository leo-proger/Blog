package com.github.leo_proger.blog.controllers;

import com.github.leo_proger.blog.dto.PostDTO;
import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/create")
    public String createGet(Model model) {
        model.addAttribute("post", new PostDTO("", null, ""));
        return "create_post";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostDTO postDTO) throws IOException {
        if (postDTO.text() == null && postDTO.imageFile() == null && postDTO.imageUrl().isEmpty()) {
            return "ERROR: At least one param must be not null";
        }
        if (postDTO.imageFile() != null && postDTO.imageUrl() != null) {
            return "ERROR: You can add only one image";
        }

        Post post = new Post();

        if (postDTO.text() != null) {
            post.setText(postDTO.text());
        }
        if (postDTO.imageUrl() != null) {
            post.setImageUrl(postDTO.imageUrl());
        }
        if (postDTO.imageFile() != null) {
            post.setImage(postDTO.imageFile().getBytes());
        }
        postService.save(post);
        return "redirect:/";
    }
}
