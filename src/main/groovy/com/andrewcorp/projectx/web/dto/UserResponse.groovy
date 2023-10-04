package com.andrewcorp.projectx.web.dto
/**
 *
 * @author Andrew
 * @since 04.10.2023
 */
record UserResponse(
        String id,
        String username,
        String fullName,
        List<String> followers,
        List<String> following,
        String token) implements Serializable {}