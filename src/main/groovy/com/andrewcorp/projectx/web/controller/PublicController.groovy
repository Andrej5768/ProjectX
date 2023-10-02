package com.andrewcorp.projectx.web.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@RestController
@RequestMapping("/api/public")
class PublicController {

    @GetMapping("/ping")
    ResponseEntity<?> ping() {
        return ResponseEntity.ok().build()
    }
}
