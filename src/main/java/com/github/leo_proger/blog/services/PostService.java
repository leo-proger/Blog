package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }
}
