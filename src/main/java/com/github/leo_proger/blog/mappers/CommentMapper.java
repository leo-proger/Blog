package com.github.leo_proger.blog.mappers;

import com.github.leo_proger.blog.dtos.CommentDTO;
import com.github.leo_proger.blog.models.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getAuthor().getUsername(),
                comment.getPost().getId(),
                comment.getText()
        );
    }
}
