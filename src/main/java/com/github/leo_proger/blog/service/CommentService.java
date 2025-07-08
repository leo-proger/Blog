package com.github.leo_proger.blog.service;

import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.exception.PostNotFoundException;
import com.github.leo_proger.blog.exception.UserNotFoundException;
import com.github.leo_proger.blog.mapper.CommentMapper;
import com.github.leo_proger.blog.model.Comment;
import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.CommentRepository;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }


    public CommentDTO createComment(String authorName, Long postID, String text) {
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
        return commentMapper.toDTO(commentRepository.save(comment));
    }
}
