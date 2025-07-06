package com.github.leo_proger.blog.dto;

import com.github.leo_proger.blog.models.Comment;

import java.time.LocalDateTime;

public record CommentDTO(
        Long postID,
        String authorName,
        String text,
        LocalDateTime createdAt
) {
    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getPost().getId(),
                comment.getAuthor().getUsername(),
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}
