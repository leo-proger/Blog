package com.github.leo_proger.blog.dto;

import com.github.leo_proger.blog.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long postID;
    private String authorName;
    private String text;
    private LocalDateTime createdAt;


    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getPost().getId(),
                comment.getAuthor().getUsername(),
                comment.getText(),
                comment.getCreatedAt()
        );
    }
}
