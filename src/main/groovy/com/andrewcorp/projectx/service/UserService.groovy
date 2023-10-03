package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.security.JwtProvider
import com.andrewcorp.projectx.web.dto.UserDTO
import com.andrewcorp.projectx.web.dto.UserPayload
import com.andrewcorp.projectx.web.dto.UserRegisterRequest
import com.andrewcorp.projectx.web.error.InvalidPasswordException
import com.andrewcorp.projectx.web.error.UserAlreadyExistException
import com.andrewcorp.projectx.web.error.UserNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@Slf4j
@Service
class UserService {
    private final UserRepository userRepository
    private final PasswordEncoder passwordEncoder
    private final JwtProvider jwtProvider

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
        this.jwtProvider = jwtProvider
    }

    UserDTO registerUser(UserRegisterRequest userPayload) {
        log.info("Registering user with username {}", userPayload.username)
        if (userRepository.findByUsername(userPayload.username).isPresent()) {
            throw new UserAlreadyExistException("User with username ${userPayload.username} already exists")
        }
        User user = new User(
                username: userPayload.username,
                password: passwordEncoder.encode(userPayload.password),
                fullName: userPayload.fullName
        )
        User savedUser = userRepository.save(user)
        log.info("User with username {} registered", userPayload.username)
        return createUserDTO(savedUser)
    }

    UserDTO getUserById(Long userId) {
        log.info("Getting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        return createUserDTO(userFromDb)
    }

    UserDTO updateUser(Long userId, UserDTO userDTO) {
        log.info("Updating user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }

        if (userDTO.username) {
            userFromDb.username = userDTO.username
        }
        if (userDTO.fullName) {
            userFromDb.fullName = userDTO.fullName
        }
        if (userDTO.followers) {
            userFromDb.followers = userDTO.followers
        }
        if (userDTO.following) {
            userFromDb.following = userDTO.following
        }

        User updatedUser = userRepository.save(userFromDb)
        return createUserDTO(updatedUser)
    }

    void deleteUser(Long userId) {
        log.info("Deleting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        userRepository.deleteById(userId)
    }

    UserDTO loginUser(UserPayload userPayload) {
        log.info("Logging in user with username {}", userPayload.username)
        User userFromDb = userRepository.findByUsername(userPayload.username).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with username ${userPayload.username} not found")
        }
        if (!passwordEncoder.matches(userPayload.password, userFromDb.password)) {
            throw new InvalidPasswordException("Password is incorrect")
        }
        String token = jwtProvider.generateToken(userFromDb.username)
        UserDTO newUser = new UserDTO(
                id: userFromDb.id,
                username: userFromDb.username,
                fullName: userFromDb.fullName,
                followers: userFromDb.followers,
                following: userFromDb.following,
        )
        newUser.token = token
        return newUser
    }

    void unfollowUser(long userId, long unfollowedUserId) {
        log.info("Unfollowing user with id {} for user with id {}", unfollowedUserId, userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        User unfollowedUserFromDb = userRepository.findById(unfollowedUserId).orElse(null)
        if (unfollowedUserFromDb == null) {
            throw new UserNotFoundException("User with id ${unfollowedUserId} not found")
        }
        userFromDb.following.remove(unfollowedUserId)
        unfollowedUserFromDb.followers.remove(userId)
        userRepository.save(userFromDb)
        userRepository.save(unfollowedUserFromDb)
    }

    void followUser(long userId, long followedUserId) {
        log.info("Following user with id {} for user with id {}", followedUserId, userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        User followedUserFromDb = userRepository.findById(followedUserId).orElse(null)
        if (followedUserFromDb == null) {
            throw new UserNotFoundException("User with id ${followedUserId} not found")
        }
        userFromDb.following.add(followedUserId)
        followedUserFromDb.followers.add(userId)
        userRepository.save(userFromDb)
        userRepository.save(followedUserFromDb)
    }

    private UserDTO createUserDTO(User user) {
        return new UserDTO(
                id: user.id,
                username: user.username,
                fullName: user.fullName,
                followers: user.followers,
                following: user.following
        )
    }
}
