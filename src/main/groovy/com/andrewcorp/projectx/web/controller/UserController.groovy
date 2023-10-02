package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.web.dto.UserDTO
import com.andrewcorp.projectx.service.UserService
import com.andrewcorp.projectx.web.dto.UserPayload
import com.andrewcorp.projectx.web.dto.UserRegisterRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */

@RestController
@RequestMapping("/api/users")
class UserController {

    @Autowired
    private UserService userService

    @PostMapping("/register")
    ResponseEntity<UserDTO> registerUser(@RequestBody @Valid @NotNull UserRegisterRequest userPayload) {
        UserDTO createdUser = userService.registerUser(userPayload)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody @Valid @NotNull UserPayload userPayload) {
        String token = userService.loginUser(userPayload)
        return new ResponseEntity<>(token, HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId)
        return new ResponseEntity<>(user, HttpStatus.OK)
    }

    @PutMapping("/{userId}")
    ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO)
        return new ResponseEntity<>(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{userId}/unfollow/{unfollowedUserId}")
    ResponseEntity<UserDTO> unfollowUser(@PathVariable @NotNull Long userId, @PathVariable Long unfollowedUserId) {
        userService.unfollowUser(userId, unfollowedUserId)
        return new ResponseEntity<>(HttpStatus.OK)
    }

    @PostMapping("/{userId}/follow/{followedUserId}")
    ResponseEntity<UserDTO> followUser(@PathVariable Long userId, @PathVariable Long followedUserId) {
        UserDTO user = userService.followUser(userId, followedUserId)
        return new ResponseEntity<>(user, HttpStatus.OK)
    }
}