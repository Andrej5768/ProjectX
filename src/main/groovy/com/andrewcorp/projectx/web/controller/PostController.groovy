package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.LikeService
import com.andrewcorp.projectx.service.PostService
import com.andrewcorp.projectx.web.dto.CommentDTO
import com.andrewcorp.projectx.web.dto.PostDTO
import com.andrewcorp.projectx.web.error.PostNotFoundException
import com.andrewcorp.projectx.web.error.UserNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
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

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@RestController
@RequestMapping("/api/posts")
class PostController {
    private final PostService postService
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping("/create")
    ResponseEntity<?> createPost(@RequestBody @Valid PostDTO postDTO) {
        try {
            def newPost = postService.createPost(postDTO)
            HttpHeaders headers = new HttpHeaders()
            println newPost
            headers.set("Content-Type", "application/json")
            return new ResponseEntity<?>(newPost, headers, HttpStatus.CREATED)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{postId}/update")
    ResponseEntity<PostDTO> updatePost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {
        try {
            def updatedPost = postService.updatePost(postId, postDTO)
            return ResponseEntity.ok(updatedPost)
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{postId}/delete")
    ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId)
            return ResponseEntity.noContent().build()
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{postId}")
    ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        try {
            def post = postService.getPost(postId)
            return ResponseEntity.ok(post)
        } catch (PostNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        try {
            def posts = postService.getPostsByUser(userId)
            return ResponseEntity.ok(posts)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/feed/{userId}")
    ResponseEntity<List<PostDTO>> getFeedByUser(@PathVariable Long userId) {
        try {
            def posts = postService.getFeedByUser(userId)
            return ResponseEntity.ok(posts)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{postId}/like/{userId}")
    ResponseEntity<PostDTO> likePost(@PathVariable Long postId, @PathVariable Long userId) {
        try {
            def likedPost = postService.likePost(postId, userId)
            return ResponseEntity.ok(likedPost)
        } catch (PostNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{postId}/unlike/{userId}")
    ResponseEntity<PostDTO> unlikePost(@PathVariable Long postId, @PathVariable Long userId) {
        try {
            def unlikedPost = postService.unlikePost(postId, userId)
            return ResponseEntity.ok(unlikedPost)
        } catch (PostNotFoundException | UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }
}