package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.Comment
import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.persistence.repository.CommentRepository
import com.andrewcorp.projectx.web.dto.CommentDTO
import com.andrewcorp.projectx.web.dto.PostDTO
import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.repository.PostRepository
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.web.error.PostNotFoundException
import com.andrewcorp.projectx.web.error.UserNotFoundException
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@Slf4j
@Service
class PostService {
    private final PostRepository postRepository
    private final UserRepository userRepository
    private final LikeService likeService
    private final CommentRepository commentRepository

    PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            LikeService likeService,
            CommentRepository commentRepository
    ) {
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.likeService = likeService
        this.commentRepository = commentRepository
    }

    PostDTO createPost(PostDTO postDTO) {
        log.info("Creating post for user with id {}", postDTO.userId)
        def user = userRepository.findById(postDTO.userId)
        if (!user.isPresent()) {
            throw new UserNotFoundException("User with id ${postDTO.userId} not found")
        }

        def post = new Post(
                userId: postDTO.userId,
                content: postDTO.content,
                timestamp: LocalDateTime.now(),
        )

        post = postRepository.save(post)
        log.info("Post with id {} created for user with id {}", post.id, postDTO.userId)
        return getPostDto(post)
    }

    PostDTO updatePost(Long postId, PostDTO postDTO) {
        log.info("Updating post with id {}", postId)
        def existingPost = postRepository.findById(postId)
        if (!existingPost.isPresent()) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        Post postFromDb = existingPost.get()
        postFromDb.content = postDTO.content
        if (postDTO.likedBy != null) {
            postFromDb.likes = likeService.getLikesForPost(postId)
        }

        def post = postRepository.save(postFromDb)
        log.info("Post with id {} updated", postId)
        return getPostDto(post)
    }

    void deletePost(Long postId) {
        log.info("Deleting post with id {}", postId)
        def existingPost = postRepository.findById(postId)
        if (!existingPost.isPresent()) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        log.info("Deleting likes for post with id {}", postId)
        likeService.deleteLikesByPost(postId)
        postRepository.deleteById(postId)
    }

    PostDTO getPost(Long postId) {
        log.info("Getting post with id {}", postId)
        def existingPost = postRepository.findById(postId)
        if (!existingPost.isPresent()) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        log.info("Post with id {} found", postId)
        return getPostDto(existingPost.get())
    }

    List<PostDTO> getPostsByUser(Long userId) {
        log.info("Getting posts for user with id {}", userId)
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        List<Post> posts = postRepository.findAllByUserId(userId)
        log.info("Found {} posts for user with id {}", posts.size(), userId)
        return posts.collect { post -> getPostDto(post)}
    }

    List<PostDTO> getFeedByUser(Long userId) {
        log.info("Getting feed for user with id {}", userId)
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        List<Post> posts = postRepository.findAllByUserIdIn(user.following)
        log.info("Found {} posts for user with id {}", posts.size(), userId)
        return posts.collect { post -> getPostDto(post)}
    }

    PostDTO likePost(long postId, long userId) {
        log.info("Liking post with id {} for user with id {}", postId, userId)
        Post post = postRepository.findById(postId).orElse(null)
        if (post == null) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        post.likes.add(likeService.likePost(post, user.id, user.username))
        post = postRepository.save(post)
        return getPostDto(post)
    }

    PostDTO unlikePost(long postId, long userId) {
        log.info("Unliking post with id {} for user with id {}", postId, userId)
        Post post = postRepository.findById(postId).orElse(null)
        if (post == null) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        Like like = post.likes.find { it.userId == userId }
        post.likes.remove(like)
        likeService.unlikePost(postId, userId)
        post = postRepository.save(post)
        return getPostDto(post)
    }

    List<CommentDTO> getCommentsForPost(long postId) {
        log.info("Getting comments for post with id {}", postId)
        Post post = postRepository.findById(postId).orElse(null)
        if (post == null) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        return post.comments.collect { comment ->
            new CommentDTO(
                    id: comment.id,
                    username: userRepository.findById(comment.userId).get().username,
                    content: comment.content,
                    timestamp: comment.timestamp
            )
        }
    }

    CommentDTO createCommentForPost(long postId, CommentDTO commentDTO) {
        log.info("Creating comment for post with id {}", postId)
        Post post = postRepository.findById(postId).orElse(null)
        if (post == null) {
            throw new PostNotFoundException("Post with id ${postId} not found")
        }
        User user = userRepository.findByUsername(commentDTO.username).orElse(null)
        if (user == null) {
            throw new UserNotFoundException("User with username ${commentDTO.username} not found")
        }
        Comment comment = new Comment(
                userId: user.id,
                content: commentDTO.content,
                timestamp: LocalDateTime.now()
        )
        post.comments.add(comment)
        postRepository.save(post)
        commentRepository.save(comment)
        return new CommentDTO(
                id: comment.id,
                username: user.username,
                content: comment.content,
                timestamp: comment.timestamp
        )
    }

    PostDTO getPostDto(Post post) {
        return new PostDTO(
                id: post.id,
                userId: post.userId,
                content: post.content,
                timestamp: post.timestamp,
                likes: post.likes.size(),
                likedBy: post.likes.collect { like -> userRepository.findById(like.userId).get().username }
        )
    }
}
