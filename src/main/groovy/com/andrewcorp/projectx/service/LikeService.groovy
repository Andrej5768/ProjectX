package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.Like
import com.andrewcorp.projectx.persistence.model.Post
import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.web.error.LikeNotFoundException
import com.andrewcorp.projectx.web.error.PostNotFoundException
import org.springframework.stereotype.Service

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Service
class LikeService {
    def likeRepository

    Like likePost(Post post, Long userId, String username) {
        def like = new Like(postId: post.id, userId: userId, username: username)
        return likeRepository.save(like)
    }

    def unlikePost(Long postId, Long userId) {
        def like = likeRepository.findByPostIdAndUserId(postId, userId)
        if (!like) {
            throw new LikeNotFoundException("Like with postId ${postId} and userId ${userId} not found")
        }
        likeRepository.delete(like)
    }

    List<Like> getLikesForPost(Long postId) {
        return likeRepository.findAllByPostId(postId)
    }

    void deleteLikesByPost(long id) {
        likeRepository.deleteAllByPostId(id)
    }
}
