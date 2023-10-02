package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.LikeService
import com.andrewcorp.projectx.service.PostService
import com.andrewcorp.projectx.web.dto.CommentDTO
import com.andrewcorp.projectx.web.dto.PostDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@RestController
@RequestMapping("/api/posts")
class PostController {

    private final PostService postService
    private final LikeService likeService

    PostController(PostService postService, LikeService likeService) {
        this.postService = postService
        this.likeService = likeService
    }

    @PostMapping
    PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO)
    }

    @GetMapping("/{postId}")
    PostDTO getPost(@PathVariable Long postId) {
        return postService.getPost(postId)
    }

    @PutMapping("/{postId}")
    PostDTO updatePost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {
        return postService.updatePost(postId, postDTO)
    }

    @DeleteMapping("/{postId}")
    void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId)
    }

    @GetMapping("/user/{userId}")
    List<PostDTO> getPostsByUser(@PathVariable Long userId) {
        return postService.getPostsByUser(userId)
    }

    @PostMapping("/{postId}/like")
    PostDTO likePost(@PathVariable Long postId) {
        return postService.likePost(postId)
    }

    @DeleteMapping("/{postId}/like")
    PostDTO unlikePost(@PathVariable Long postId) {
        return postService.unlikePost(postId)
    }

    @GetMapping("/{postId}/comments")
    List<CommentDTO> getCommentsForPost(@PathVariable Long postId) {
        return postService.getCommentsForPost(postId)
    }

    @PostMapping("/{postId}/comments")
    CommentDTO createCommentForPost(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        return postService.createCommentForPost(postId, commentDTO)
    }
}
