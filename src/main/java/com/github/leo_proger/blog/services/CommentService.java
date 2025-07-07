package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.exceptions.PostNotFoundException;
import com.github.leo_proger.blog.exceptions.UserNotFoundException;
import com.github.leo_proger.blog.models.Comment;
import com.github.leo_proger.blog.models.Post;
import com.github.leo_proger.blog.models.User;
import com.github.leo_proger.blog.repositories.CommentRepository;
import com.github.leo_proger.blog.repositories.PostRepository;
import com.github.leo_proger.blog.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public void createComment(String authorName, Long postID, String text) {
        Comment comment = new Comment();

        Optional<User> authorOptional = userRepository.findByUsername(authorName);
        if (authorOptional.isPresent()) {
            comment.setAuthor(authorOptional.get());
        } else {
            throw new UserNotFoundException("User \"" + authorName + "\" not found");
        }

        Optional<Post> postOptional = postRepository.findById(postID);
        if (postOptional.isPresent()) {
            comment.setPost(postOptional.get());
        } else {
            throw new PostNotFoundException("Post with id " + postID + " not found");
        }

        comment.setText(text);
        commentRepository.save(comment);
    }
}
