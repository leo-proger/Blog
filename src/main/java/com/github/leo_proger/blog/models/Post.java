package com.github.leo_proger.blog.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Base64;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @Lob
    private String image;

    private LocalDateTime createdAt;

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public void setImageUrl(String img) {
        image = img;
    }

    public void setImage(byte[] img) {
        image = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(img);
    }
}
