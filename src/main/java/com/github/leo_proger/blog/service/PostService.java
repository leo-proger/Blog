package com.github.leo_proger.blog.service;

import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    public String likePost(Long postID, Long detachedUserID) {
        // Since it's a transaction, new session has started and Hibernate isn't capturing changes for user from controller
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postID + " does not exist"));
        User user = userRepository.findById(detachedUserID)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + postID + " does not exist"));

        Set<User> usersLiked = post.getUsersLiked();

        if (usersLiked.contains(user) && user.getLikedPosts().contains(post)) {
            post.removeLike(user);
            user.removeLikedPost(post);
            return "REMOVE";
        } else {
            post.addLike(user);
            user.addLikedPost(post);
            return "ADD";
        }
    }
}
