package com.github.leo_proger.blog.dto;

import com.github.leo_proger.blog.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private final String authorName;
    private final Long postID;
    private final String text;

    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getAuthor().getUsername(),
                comment.getPost().getId(),
                comment.getText()
        );
    }
}
