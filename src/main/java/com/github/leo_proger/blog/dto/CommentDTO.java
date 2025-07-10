package com.github.leo_proger.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDTO {
    @NotEmpty(message = "Author can't be empty")
    @NotBlank(message = "Author can't be empty")
    private final String authorName;

    @NotNull(message = "Post ID can't be empty")
    private final Long postID;

    @NotEmpty(message = "Text can't be empty")
    @NotBlank(message = "Text can't be empty")
    private final String text;
}
