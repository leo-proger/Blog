package com.github.leo_proger.blog.repositories;

import com.github.leo_proger.blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findCommentsByPostId(Long postID);
}
