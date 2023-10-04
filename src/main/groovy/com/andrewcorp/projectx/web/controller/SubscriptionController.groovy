package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.FollowService;
import com.andrewcorp.projectx.service.UserService;
import com.andrewcorp.projectx.web.error.UserNotFoundException
import com.sun.jdi.request.DuplicateRequestException
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.context.SecurityContextHolder

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Slf4j
@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionController {

    @Autowired
    private FollowService followService

    @PostMapping("/subscribe")
    ResponseEntity<Void> subscribeUser(@RequestParam("targetUserId") String targetUserId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        try {
            followService.followUser(userId, targetUserId)
            return new ResponseEntity<>(HttpStatus.OK)
        } catch (DuplicateRequestException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/unsubscribe")
    ResponseEntity<Void> unsubscribeUser(@RequestParam("targetUserId") String targetUserId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        try {
            followService.unfollowUser(userId, targetUserId)
            return new ResponseEntity<>(HttpStatus.OK)
        } catch (DuplicateRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST)
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
    }
}
