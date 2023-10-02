package com.andrewcorp.projectx.persistence.model;

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {

    @Id
    private Long id
    private String username
    private String password
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

    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
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