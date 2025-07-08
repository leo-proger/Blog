package com.github.leo_proger.blog.mapper;

import com.github.leo_proger.blog.dto.CommentDTO;
import com.github.leo_proger.blog.model.Comment;
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
