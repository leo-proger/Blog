package com.github.leo_proger.blog.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PostComment {

    @EmbeddedId
    private PostCommentKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @MapsId("postID")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public PostComment() {
        this.createdAt = LocalDateTime.now();
    }
}
