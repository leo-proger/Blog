package com.github.leo_proger.blog.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Lob
    private String image;

    private LocalDateTime createdAt;

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public void setImageUrl(String img) {
        if (img != null && !img.isEmpty()) {
            image = img;
            return;
        }
        throw new IllegalArgumentException("Image URL can't be empty");
    }

    public void setImage(byte[] img, String filetype) {
        if (img != null && img.length != 0) {
            image = "data:" + filetype + ";base64," + Base64.getEncoder().encodeToString(img);
            return;
        }
        throw new IllegalArgumentException("Image file can't be empty");
    }
}
