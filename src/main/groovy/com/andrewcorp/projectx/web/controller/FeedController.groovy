package com.andrewcorp.projectx.web.controller

import com.andrewcorp.projectx.service.FeedService
import com.andrewcorp.projectx.web.dto.PagedFeedDTO
import com.andrewcorp.projectx.web.error.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

/**
 *
 * @author Andrew
 * @since 04.10.2023
 */
@RestController
@RequestMapping("/api/feed")
class FeedController {
    private final FeedService feedService

    @Autowired
    FeedController(FeedService feedService) {
        this.feedService = feedService
    }

    @GetMapping
    ResponseEntity<PagedFeedDTO> getUserStream(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        try {
            def userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
            def feed = feedService.getUserFeed(userId, PageRequest.of(page, pageSize))
            return ResponseEntity.ok(feed)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<PagedFeedDTO> getUserFeed(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            def feed = feedService.getUserFeed(userId, PageRequest.of(page, pageSize))
            return ResponseEntity.ok(feed)
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build()
        }
    }
}
