package com.github.leo_proger.blog.service;


import com.github.leo_proger.blog.model.Post;
import com.github.leo_proger.blog.model.PostLike;
import com.github.leo_proger.blog.model.User;
import com.github.leo_proger.blog.repository.PostLikeRepository;
import com.github.leo_proger.blog.repository.PostRepository;
import com.github.leo_proger.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PostLikeRepository postLikeRepository;

	public PostService(
			PostRepository postRepository,
			UserRepository userRepository,
			PostLikeRepository postLikeRepository
	) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postLikeRepository = postLikeRepository;
	}

	public List<Post> findAllByOrderByCreatedAtDesc() {
		return postRepository.findAllByOrderByCreatedAtDesc();
	}

	public void save(Post post) {
		postRepository.save(post);
	}

	public void deleteById(Long id) {
		if (!postRepository.existsById(id)) throw new IllegalArgumentException(
				"Post with id " + id + " does not exist");

		postRepository.deleteById(id);
	}

	public String likePost(Long postID, Long detachedUserID) {
		// Since it's a transaction, new session has started and Hibernate isn't capturing changes for user from controller
		Post post = postRepository.findById(postID).orElseThrow(() -> new IllegalArgumentException(
				"Post with id " + postID + " does not exist"));
		User user = userRepository.findById(detachedUserID).orElseThrow(() -> new IllegalArgumentException(
				"User with id " + detachedUserID + " does not exist"));

		Optional<PostLike> postLikeOptional = postLikeRepository.findPostLikeByUserAndPost(user, post);

		if (postLikeOptional.isPresent())
		{
			postLikeRepository.delete(postLikeOptional.get());
			return "REMOVE";
		} else
		{
			PostLike postLike = new PostLike();
			postLike.setPost(post);
			postLike.setUser(user);

			postLikeRepository.save(postLike);
			return "ADD";
		}
	}

}
