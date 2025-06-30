package com.github.leo_proger.blog.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    private final LocalDateTime createdAt;

    private Set<Long> usersLiked = new HashSet<>(); // Users who liked the post

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public void setImageUrl(String img) {
        if (img == null || img.isEmpty()) {
            throw new IllegalArgumentException("Image URL can't be empty");
        }
        image = img;
    }

    public void setImage(byte[] img, String filetype) {
        if (img == null || img.length == 0) {
            throw new IllegalArgumentException("Image file can't be empty");
        }
        image = "data:" + filetype + ";base64," + Base64.getEncoder().encodeToString(img);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addLike(Long userID) {
        usersLiked.add(userID);
    }

    public void removeLike(Long userID) {
        usersLiked.remove(userID);
    }

    public Integer getLikesCount() {
        return usersLiked.size();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", createdAt=" + createdAt +
                ", usersLiked=" + usersLiked +
                '}';
    }
}
