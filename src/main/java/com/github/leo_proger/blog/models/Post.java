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

    @ManyToMany(mappedBy = "likedPosts", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private final Set<User> usersLiked = new HashSet<>(); // Users who liked the post

    @OneToMany(mappedBy = "post")
    private final Set<PostComment> comments = new HashSet<>(); // Comments on the post

    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public void addLike(User user) {
        usersLiked.add(user);
    }

    public void removeLike(User user) {
        usersLiked.remove(user);
    }

    public Integer getLikesCount() {
        return usersLiked.size();
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
}
