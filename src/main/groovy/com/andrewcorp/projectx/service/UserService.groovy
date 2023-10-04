package com.andrewcorp.projectx.service

import com.andrewcorp.projectx.persistence.model.User
import com.andrewcorp.projectx.persistence.repository.UserRepository
import com.andrewcorp.projectx.security.JwtProvider
import com.andrewcorp.projectx.web.dto.UserDTO
import com.andrewcorp.projectx.web.dto.UserPayload
import com.andrewcorp.projectx.web.dto.UserRegisterRequest
import com.andrewcorp.projectx.web.dto.UserResponse
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
        if (userRepository.existsByUsername(userPayload.username)) {
            log.error("User with username {} already exists", userPayload.username)
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

    UserDTO getUserById(String userId) {
        log.info("Getting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        return createUserDTO(userFromDb)
    }

    UserDTO updateUser(String userId, UserDTO userDTO) {
        log.info("Updating user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            log.error("User with id {} not found", userId)
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

    void deleteUser(String userId) {
        log.info("Deleting user with id {}", userId)
        User userFromDb = userRepository.findById(userId).orElse(null)
        if (userFromDb == null) {
            log.error("User with id {} not found", userId)
            throw new UserNotFoundException("User with id ${userId} not found")
        }
        userRepository.deleteById(userId)
    }

    UserResponse loginUser(UserPayload userPayload) {
        log.info("Logging in user with username {}", userPayload.username)
        User userFromDb = userRepository.findByUsername(userPayload.username).orElse(null)
        if (userFromDb == null) {
            log.error("User with username {} not found", userPayload.username)
            throw new UserNotFoundException("User with username ${userPayload.username} not found")
        }
        if (!passwordEncoder.matches(userPayload.password, userFromDb.password)) {
            log.error("Password is incorrect")
            throw new InvalidPasswordException("Password is incorrect")
        }
        String token = jwtProvider.generateToken(userFromDb.username, userFromDb.id)
        UserResponse newUser = new UserResponse(
                id: userFromDb.id,
                username: userFromDb.username,
                fullName: userFromDb.fullName,
                followers: userFromDb.followers,
                following: userFromDb.following,
                token: token
        )
        return newUser
    }

    private static UserDTO createUserDTO(User user) {
        return new UserDTO(
                id: user.id,
                username: user.username,
                fullName: user.fullName,
                followers: user.followers,
                following: user.following
        )
    }

    void logout(String userId, String token) {
        log.info("Invalidating token for user id {}", userId)
        jwtProvider.invalidateToken(token)
    }
}
