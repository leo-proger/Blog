package com.github.leo_proger.blog.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "text", nullable = false)
    private String text;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }
}
