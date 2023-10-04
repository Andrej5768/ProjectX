package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.Comment
import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.repository.CommentRepository
import com.andrewcorp.projectx.persistence.repository.LikeRepository
import com.andrewcorp.projectx.persistence.repository.PostRepository
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.web.dto.CommentDTO
import com.andrewcorp.projectx.web.dto.LikeDTO
import com.andrewcorp.projectx.web.dto.PagedFeedDTO
import com.andrewcorp.projectx.web.dto.PostDTO
import com.andrewcorp.projectx.web.error.UserNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Slf4j
@Service
class FeedService {
    private final FollowService followService
    private final PostRepository postRepository
    private final LikeRepository likeRepository
    private final CommentRepository commentRepository
    private final UserRepository userRepository

    @Autowired
    FeedService(FollowService followService,
                PostRepository postRepository,
                LikeRepository likeRepository,
                CommentRepository commentRepository,
                UserRepository userRepository) {
        this.followService = followService
        this.postRepository = postRepository
        this.likeRepository = likeRepository
        this.commentRepository = commentRepository
        this.userRepository = userRepository
    }

    PagedFeedDTO getUserFeed(String userId, Pageable pageable) {
        log.info("Getting feed for user with id {}", userId)
        if (!userRepository.existsById(userId)) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        List<String> followingUserIds = followService.getFollowing(userId)

        Page<Post> posts = postRepository.findAllByUserIdIn(followingUserIds, pageable)
        Page<Comment> comments = commentRepository.findAllByUserIdIn(followingUserIds, pageable)
        Page<Like> likes = likeRepository.findAllByUserIdIn(followingUserIds, pageable)
        def totalElements = posts.totalElements + comments.totalElements + likes.totalElements
        def totalPages = posts.totalPages + comments.totalPages + likes.totalPages
        def pagedFeedDTO = new PagedFeedDTO(
                posts: getPosts(posts),
                likes: getLikes(likes),
                comments: getComments(comments),
                currentPage: pageable.pageNumber,
                pageSize: pageable.pageSize,
                totalItems: totalElements,
                totalPages: totalPages
        )
        log.info("Returning feed for user with id {}", userId)
        return pagedFeedDTO
    }

     static List<PostDTO> getPosts(Page<Post> posts) {
        List<PostDTO> postDTOs = new ArrayList<>()
        if (posts == null) {
            return postDTOs
        }
        posts.each { post ->
            def postDTO = new PostDTO(
                    id: post.id,
                    userId: post.userId,
                    postContent: post.content,
                    timestamp: post.timestamp,
                    likes: post.likes == null ? 0 : post.likes.size(),
                    likedBy: post.likes == null ? [] : (post.likes.collect { it.username }),
                    comments: post.comments
            )
            postDTOs.add(postDTO)
        }
        return postDTOs
    }

     static List<LikeDTO> getLikes(Page<Like> likes) {
        List<LikeDTO> likeDTOs = new ArrayList<>()
        if (likes == null) {
            return likeDTOs
        }
        likes.each { like ->
            def likeDTO = new LikeDTO(
                    id: like.id,
                    userId: like.userId,
                    postId: like.postId,
                    timestamp: like.timestamp
            )
            likeDTOs.add(likeDTO)
        }
        return likeDTOs
    }

     static List<CommentDTO> getComments(Page<Comment> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>()
        if (comments == null) {
            return commentDTOs
        }
        comments.each { comment ->
            def commentDTO = new CommentDTO(
                    id: comment.id,
                    username: comment.username,
                    postId: comment.postId,
                    commentContent: comment.commentContent,
                    timestamp: comment.timestamp
            )
            commentDTOs.add(commentDTO)
        }
        return commentDTOs
    }
}
