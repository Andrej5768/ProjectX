package com.andrewcorp.projectx.web.controller;

import com.andrewcorp.projectx.service.UserService;
import com.andrewcorp.projectx.web.error.UserNotFoundException;
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
@RequestMapping("/api/subscriptions")
class SubscriptionController {

    @Autowired
    private UserService userService

    @PostMapping("/subscribe")
    ResponseEntity<Void> subscribeUser(@RequestParam("userId") Long userId, @RequestParam("targetUserId") Long targetUserId) {
        try {
            userService.followUser(userId, targetUserId)
            return new ResponseEntity<>(HttpStatus.OK)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/unsubscribe")
    ResponseEntity<Void> unsubscribeUser(@RequestParam("userId") Long userId, @RequestParam("targetUserId") Long targetUserId) {
        try {
            userService.unfollowUser(userId, targetUserId)
            return new ResponseEntity<>(HttpStatus.OK)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }
}
