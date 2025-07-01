package com.github.leo_proger.blog.services;

import com.github.leo_proger.blog.models.Comment;
import com.github.leo_proger.blog.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Set<Comment> findCommentsByPostId(Long postID) {
        return commentRepository.findCommentsByPostId(postID);
    }
}
