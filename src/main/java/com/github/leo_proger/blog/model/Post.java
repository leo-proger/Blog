package com.github.leo_proger.blog.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;


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

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private final Set<PostLike> likes = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private final Set<Comment> comments = new HashSet<>();

	public Post() {
		this.createdAt = LocalDateTime.now();
	}

	public Integer getCommentsCount() {
		return comments.size();
	}

	public List<Comment> getCommentsByCreatedAtDesc() {
		return comments.stream().sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())).toList();
	}

	public void setImageUrl(String img) {
		if (img == null || img.isEmpty())
		{
			throw new IllegalArgumentException("Image URL can't be empty");
		}
		image = img;
	}

	public void setImage(byte[] img, String filetype) {
		if (img == null || img.length == 0)
		{
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

	@Override
	public String toString() {
		return "Post{" + "id=" + id + ", text='" + text + '\'' + ", image='" + image + '\'' + ", createdAt=" +
		       createdAt + '}';
	}

}
