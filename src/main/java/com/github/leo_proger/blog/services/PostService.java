package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.repositories.PostRepository;
import com.github.leo_proger.blog.repositories.UserRepository;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
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

    public void likePost(Long postID, Long detachedUserID) {
        // Since it's a transaction, new session has started and Hibernate isn't capturing changes for user from controller
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postID + " does not exist"));
        User user = userRepository.findById(detachedUserID)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + postID + " does not exist"));

        Set<User> usersLiked = post.getUsersLiked();

        if (usersLiked.contains(user) && user.getLikedPosts().contains(post)) {
            post.removeLike(user);
            user.removeLikedPost(post);
        } else {
            post.addLike(user);
            user.addLikedPost(post);
        }
    }
}
