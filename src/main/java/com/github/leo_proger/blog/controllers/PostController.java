package com.github.leo_proger.blog.controllers;

import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.services.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public String createPost(@RequestParam(required = false) String text,
                             @RequestParam(required = false) MultipartFile image,
                             @RequestParam(required = false) String imageUrl) throws IOException {
        if (text == null && image == null && imageUrl == null) {
            return "ERROR: At least one param must be not null";
        }
        if (image != null && imageUrl != null) {
            return "ERROR: You can add only one image";
        }

        Post post = new Post();

        if (text != null) {
            post.setText(text);
        }
        if (imageUrl != null) {
            post.setImageUrl(imageUrl);
        }
        if (image != null) {
            post.setImage(image.getBytes());
        }
        postService.save(post);
        return "Post created";
    }
}
