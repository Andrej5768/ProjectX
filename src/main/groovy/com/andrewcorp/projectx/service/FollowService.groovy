package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.web.error.UserNotFoundException
import com.sun.jdi.request.DuplicateRequestException
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Slf4j
@Service
class FollowService {
private final UserRepository userRepository

    FollowService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    void unfollowUser(String userId, String unfollowedUserId) {
        log.info("Unfollowing user with id {} for user with id {}", unfollowedUserId, userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        User unfollowedUserFromDb = userRepository.findById(unfollowedUserId).orElse(null)
        if (unfollowedUserFromDb == null) {
            log.error("User with id {} not found", unfollowedUserId)
            throw new UserNotFoundException("User with id ${unfollowedUserId} not found")
        }
        if (userFromDb.following == null) {
            userFromDb.following = []
        }
        if (unfollowedUserFromDb.followers == null) {
            unfollowedUserFromDb.followers = []
        }
        if ((userFromDb.following.size() == 0 || !userFromDb.following.contains(unfollowedUserId))
                || (unfollowedUserFromDb.followers.size() == 0 || !unfollowedUserFromDb.followers.contains(userId))) {
            log.error("User with id {} does not follow user with id {}", userId, unfollowedUserId)
            throw new DuplicateRequestException("User with id ${userId} does not follow user with id ${unfollowedUserId}")
        }

        userFromDb.following.remove(unfollowedUserId)
        unfollowedUserFromDb.followers.remove(userId)
        userRepository.save(userFromDb)
        userRepository.save(unfollowedUserFromDb)
    }

    void followUser(String userId, String followedUserId) {
        log.info("Following user with id {} for user with id {}", followedUserId, userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        User followedUserFromDb = userRepository.findById(followedUserId).orElse(null)
        if (followedUserFromDb == null) {
            log.error("User with id {} not found", followedUserId)
            throw new UserNotFoundException("User with id ${followedUserId} not found")
        }
        if (userFromDb.following == null) {
            userFromDb.following = []
        }
        if (followedUserFromDb.followers == null) {
            followedUserFromDb.followers = []
        }
        if ((userFromDb.following.size() > 0 && userFromDb.following.contains(followedUserId))
                || (followedUserFromDb.followers.size() > 0 && followedUserFromDb.followers.contains(userId))) {
            log.error("User with id {} already follows user with id {}", userId, followedUserId)
            throw new DuplicateRequestException("User with id ${userId} already follows user with id ${followedUserId}")
        }
        userFromDb.following.add(followedUserId)
        followedUserFromDb.followers.add(userId)
        userRepository.save(userFromDb)
        userRepository.save(followedUserFromDb)
    }

    List<String> getFollowers(String userId) {
        log.info("Getting followers for user with id {}", userId)
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        log.info("Found {} followers for user with id {}", user.followers.size(), userId)
        return user.followers
    }

    List<String> getFollowing(String userId) {
        log.info("Getting following for user with id {}", userId)
        User user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        if (user.following == null) {
            user.following = []
        }
        log.info("Found {} following for user with id {}", user.following.size(), userId)
        return user.following
    }
}
