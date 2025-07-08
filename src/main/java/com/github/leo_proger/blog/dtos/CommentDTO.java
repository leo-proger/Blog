package com.github.leo_proger.blog.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private final String authorName;
    private final Long postID;
    private final String text;
}
