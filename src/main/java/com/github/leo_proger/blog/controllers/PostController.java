package com.github.leo_proger.blog.controllers;

import com.github.leo_proger.blog.dtos.PostDTO;
import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;


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
    public String createPost(@Valid @ModelAttribute("post") PostDTO postDTO, BindingResult bindingResult) throws IOException {
        String postText = postDTO.getText();
        String postImageUrl = postDTO.getImageUrl();
        MultipartFile postImageFile = postDTO.getImageFile();

        if ((postText == null || postText.isBlank())
                && (postImageUrl == null || postImageUrl.isBlank())
                && (postImageFile == null || postImageFile.isEmpty())) {
            bindingResult.rejectValue("text", "error.text", "At least one param must be not null");
        }
        if (postImageFile != null && !postImageFile.isEmpty()
                && postImageUrl != null && !postImageUrl.isBlank()) {
            bindingResult.rejectValue("imageUrl", "error.image", "You can add only one image");
        }

        Post post = new Post();

        if (postText != null && !postText.isBlank()) {
            post.setText(postText);
        }
        if (postImageUrl != null && !postImageUrl.isBlank()) {
            try {
                URL url = new URL(postImageUrl);

                URLConnection connection = url.openConnection();
                connection.connect();

                String contentType = connection.getContentType();

                if (contentType != null && contentType.startsWith("image/")) {
                    post.setImageUrl(postImageUrl);
                } else {
                    bindingResult.rejectValue("imageUrl", "error.image", "You can paste links only with images");
                }
            } catch (IOException e) {
                bindingResult.rejectValue("imageUrl", "error.image", "Incorrect URL");
            }
        }
        if (postImageFile != null && !postImageFile.isEmpty()) {
            String filetype = postImageFile.getContentType();
            if (filetype != null && filetype.startsWith("image/")) {
                post.setImage(postImageFile.getBytes(), filetype);
            } else {
                bindingResult.rejectValue("imageFile", "error.image", "Incorrect filetype. You can upload only images");
            }
        }
        if (bindingResult.hasErrors()) {
            return "create_post";
        }

        postService.save(post);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/like/{postID}")
    public ResponseEntity<?> likePost(@PathVariable Long postID, @AuthenticationPrincipal User user) {
        String action = postService.likePost(postID, user.getId());
        return new ResponseEntity<>(Map.of("Action", action), HttpStatus.OK);
    }
}
