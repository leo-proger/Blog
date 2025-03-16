package com.github.leo_proger.blog.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
