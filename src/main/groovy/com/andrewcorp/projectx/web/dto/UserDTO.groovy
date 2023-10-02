package com.andrewcorp.projectx.web.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserDTO {

    private Long id
    private String username
    private String fullName
    private List<Long> followers
    private List<Long> following

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getFullName() {
        return fullName
    }

    void setFullName(String fullName) {
        this.fullName = fullName
    }

    List<Long> getFollowers() {
        return followers
    }

    void setFollowers(List<Long> followers) {
        this.followers = followers
    }

    List<Long> getFollowing() {
        return following
    }

    void setFollowing(List<Long> following) {
        this.following = following
    }
}
