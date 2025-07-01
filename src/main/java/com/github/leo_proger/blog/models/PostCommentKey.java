package com.github.leo_proger.blog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class PostCommentKey implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "post_id", nullable = false)
    private Long postID;


    public PostCommentKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PostCommentKey that)) return false;
        return Objects.equals(userID, that.userID) && Objects.equals(postID, that.postID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, postID);
    }
}
