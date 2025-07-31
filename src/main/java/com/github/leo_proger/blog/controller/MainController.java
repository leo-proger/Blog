package com.github.leo_proger.blog.controller;


import com.github.leo_proger.blog.exception.UserNotFoundException;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.UserRepository;
import com.github.leo_proger.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class MainController {

	private final PostService postService;
	private final UserRepository userRepository;

	public MainController(PostService postService, UserRepository userRepository) {
		this.postService = postService;
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	public String index(Model model, Principal principal) {
		model.addAttribute("posts", postService.findAllByOrderByCreatedAtDesc());

		if (principal != null)
		{
			Optional<User> userOptional = userRepository.findByUsername(principal.getName());
			User user = userOptional.orElseThrow(() -> new UserNotFoundException(
					"User with name \"" + principal.getName() + "\" not found"));

			Set<Long> likedPostsIDs = user.getLikes().stream().map(postLike -> postLike.getPost().getId()).collect(
					Collectors.toSet());
			model.addAttribute("likedPostsIDs", likedPostsIDs);
		}

		return "main";
	}

}
