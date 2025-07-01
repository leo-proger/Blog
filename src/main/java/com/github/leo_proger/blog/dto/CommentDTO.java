package com.github.leo_proger.blog.dto;

import com.github.leo_proger.blog.models.Comment;

import java.time.LocalDateTime;

public record CommentDTO(
        String authorName,
        String text,
        LocalDateTime createdAt
) {
    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getAuthor().getUsername(),
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}
