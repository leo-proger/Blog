package com.github.leo_proger.blog.repository;


import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.PostLike;
import com.github.leo_proger.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	Optional<PostLike> findPostLikeByUserAndPost(User user, Post post);

	boolean existsPostLikeByUserAndPost(User user, Post post);

}
