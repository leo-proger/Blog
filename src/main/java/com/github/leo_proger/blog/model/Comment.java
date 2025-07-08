package com.github.leo_proger.blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, unique = true)
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "text", nullable = false)
    private String text;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }
}
