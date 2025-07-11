package com.github.leo_proger.blog.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter // It is not a redundant annotation
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDTO {
    private String text;

    private MultipartFile imageFile;

    @URL(message = "Invalid URL format")
    private String imageUrl;
}
