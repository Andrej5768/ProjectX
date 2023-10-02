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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class)

    private final UserRepository userRepository
    private final PasswordEncoder passwordEncoder
    private final JwtProvider jwtProvider

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
        this.jwtProvider = jwtProvider
    }

    UserDTO registerUser(UserRegisterRequest userPayload) {
        logger.info("Registering user with username {}", userPayload.username)
        if (userRepository.findByUsername(userPayload.username).isPresent()) {
            throw new UserAlreadyExistException("User with username ${userPayload.username} already exists")
        }
        User user = new User(
                username: userPayload.username,
                password: passwordEncoder.encode(userPayload.password),
                fullName: userPayload.fullName

        )
        User savedUser = userRepository.save(user)
        logger.info("User with username {} registered", userPayload.username)
        return new UserDTO(
                id: savedUser.id,
                username: savedUser.username,
                fullName: savedUser.fullName,
                followers: new ArrayList<Long>(),
                following: new ArrayList<Long>()
        )
    }

    UserDTO getUserById(Long userId) {
        logger.info("Getting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        return new UserDTO(
                id: userFromDb.id,
                username: userFromDb.username,
                fullName: userFromDb.fullName,
                followers: userFromDb.followers,
                following: userFromDb.following
        )
    }

    UserDTO updateUser(Long userId, UserDTO userDTO) {
        logger.info("Updating user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }

        User user = new User()
        user.id = userFromDb.id
        if (userDTO.username != null) {
            user.username = userDTO.username
        }
        if (userDTO.fullName != null) {
            user.fullName = userDTO.fullName
        }
        if (userDTO.followers != null) {
            user.followers = userDTO.followers
        }
        if (userDTO.following != null) {
            user.following = userDTO.following
        }
        User updatedUser = userRepository.save(user)
        return new UserDTO(
                id: updatedUser.id,
                username: updatedUser.username,
                fullName: updatedUser.fullName,
                followers: updatedUser.followers,
                following: updatedUser.following
        )
    }

    void deleteUser(Long userId) {
        logger.info("Deleting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        userRepository.deleteById(userId)
    }

    String loginUser(UserPayload userPayload) {
        logger.info("Logging in user with username {}", userPayload.username)
        User userFromDb = userRepository.findByUsername(userPayload.username).orElse(null)
        if (userFromDb == null) {
            throw new UserNotFoundException("User with username ${userPayload.username} not found")
        }
        if (!passwordEncoder.matches(userPayload.password, userFromDb.password)) {
            throw new InvalidPasswordException("Password is incorrect")
        }
        return jwtProvider.generateToken(userFromDb.username)
    }

    def unfollowUser(long userId, long unfollowedUserId) {
        logger.info("Unfollowing user with id {} for user with id {}", unfollowedUserId, userId)
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

    def followUser(long userId, long followedUserId) {
        logger.info("Following user with id {} for user with id {}", followedUserId, userId)
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
}