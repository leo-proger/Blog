package com.github.leo_proger.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String text;

    private MultipartFile imageFile;

    @URL(message = "Invalid URL format")
    private String imageUrl;
}
