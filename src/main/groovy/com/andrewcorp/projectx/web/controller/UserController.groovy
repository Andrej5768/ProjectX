package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.UserService
import com.andrewcorp.projectx.web.dto.UserDTO
import com.andrewcorp.projectx.web.dto.UserPayload
import com.andrewcorp.projectx.web.dto.UserRegisterRequest
import com.andrewcorp.projectx.web.dto.UserResponse
import com.andrewcorp.projectx.web.error.InvalidPasswordException
import com.andrewcorp.projectx.web.error.UserAlreadyExistException
import com.andrewcorp.projectx.web.error.UserNotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
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
        try {
            def userDTO = userService.registerUser(userPayload)
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED)
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT)
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasPermission(authentication, #userId, 'userId')")
    ResponseEntity<UserDTO> getUser(@PathVariable("userId") String userId) {
        try {
            def userDTO = userService.getUserById(userId)
            return new ResponseEntity<>(userDTO, HttpStatus.OK)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasPermission(authentication, #userId, 'userId')")
    ResponseEntity<UserDTO> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDTO userDTO) {
        try {
            def updatedUser = userService.updateUser(userId, userDTO)
            return new ResponseEntity<>(updatedUser, HttpStatus.OK)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission(authentication, #userId, 'userId')")
    ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        try {
            userService.deleteUser(userId)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody @Valid @NotNull UserPayload userPayload) {
        try {
            UserResponse user = userService.loginUser(userPayload)
            return new ResponseEntity<>(user, HttpStatus.OK)
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        try {
            userService.logout(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString(), request.getHeader("Authorization").replace("Bearer ", ""))
            SecurityContextHolder.getContext().setAuthentication(null)
            return new ResponseEntity<>(HttpStatus.OK)
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN)
        }
    }
}