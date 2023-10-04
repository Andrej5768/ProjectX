package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.LikeService
import com.andrewcorp.projectx.service.PostService
import com.andrewcorp.projectx.web.dto.CommentDTO
import com.andrewcorp.projectx.web.dto.CommentRequest
import com.andrewcorp.projectx.web.dto.PostDTO
import com.andrewcorp.projectx.web.error.LikeNotFoundException
import com.andrewcorp.projectx.web.error.PostNotFoundException
import com.andrewcorp.projectx.web.error.UserNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.context.SecurityContextHolder

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
class PostController {
    private final PostService postService

    @Autowired
    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(authentication, #postDTO.userId, 'userId')")
    ResponseEntity<PostDTO> createPost(@RequestBody @Valid PostDTO postDTO) {
        try {
            PostDTO newPost = postService.createPost(postDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission(authentication, #postDTO.userId, 'userId')")
    ResponseEntity<PostDTO> updatePost(@RequestBody @Valid PostDTO postDTO) {
        try {
            def updatedPost = postService.updatePost(postDTO.id, postDTO)
            return ResponseEntity.ok(updatedPost)
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{postId}/delete")
    ResponseEntity<Void> deletePost(@PathVariable String postId) {
        try {
            if (postService.getPost(postId).userId != SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()) {
                log.warn("Unauthorized attempt to delete post with id {}", postId)
                return new ResponseEntity<>(HttpStatus.FORBIDDEN)
            }
            postService.deletePost(postId)
            return ResponseEntity.noContent().build()
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{postId}")
    ResponseEntity<PostDTO> getPost(@PathVariable String postId) {
        try {
            def post = postService.getPost(postId)
            return ResponseEntity.ok(post)
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String userId) {
        try {
            def posts = postService.getPostsByUser(userId)
            return ResponseEntity.ok(posts)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/feed/{userId}")
    @PreAuthorize()
    ResponseEntity<List<PostDTO>> getFeedByUser(@PathVariable String userId) {
        try {
            def posts = postService.getFeedByUser(userId)
            return ResponseEntity.ok(posts)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{postId}/like")
    ResponseEntity<PostDTO> likePost(@PathVariable String postId) {
        def userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        try {
            def likedPost = postService.likePost(postId, userId)
            return ResponseEntity.ok(likedPost)
        } catch (PostNotFoundException | UserNotFoundException | LikeNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{postId}/unlike")
    ResponseEntity<PostDTO> unlikePost(@PathVariable String postId) {
        def userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        try {
            def unlikedPost = postService.unlikePost(postId, userId)
            return ResponseEntity.ok(unlikedPost)
        } catch (PostNotFoundException | UserNotFoundException | LikeNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{postId}/comment")
    ResponseEntity<CommentDTO> commentPost(@PathVariable String postId, @RequestBody @Valid CommentRequest commentRequest) {
        def userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        try {
            def commentDto = postService.createCommentForPost(postId, userId, commentRequest.commentContent)
            return ResponseEntity.ok(commentDto)
        } catch (PostNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{postId}/comments")
    ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable String postId) {
        try {
            def comments = postService.getCommentsForPost(postId)
            return ResponseEntity.ok(comments)
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }
}