package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllByOrderByCreatedAtDesc() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void deleteById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        postRepository.deleteById(id);
    }

    public void likePost(Long postID, User user) {
        if (!postRepository.existsById(postID)) {
            throw new IllegalArgumentException("Post with id " + postID + " does not exist");
        }
        Post post = postRepository.getReferenceById(postID);
        Set<Long> usersLiked = post.getUsersLiked();

        if (usersLiked.contains(user.getId())) {
            post.removeLike(user.getId());
        } else {
            post.addLike(user.getId());
        }
    }
}
