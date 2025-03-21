package com.github.leo_proger.blog.dto;

import org.springframework.web.multipart.MultipartFile;

public record PostDTO(
        String text,
        MultipartFile imageFile,
        String imageUrl
) {
}
