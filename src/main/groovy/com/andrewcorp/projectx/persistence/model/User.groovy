package com.andrewcorp.projectx.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User {

    @Id
    private String id
    private String username
    private String password
    private String fullName
    private List<String> followers
    private List<String> following

    User() {
    }

    String getId() {
        return id
    }

    void setId(String id) {
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

    List<String> getFollowers() {
        return followers
    }

    void setFollowers(List<String> followers) {
        this.followers = followers
    }

    List<String> getFollowing() {
        return following
    }

    void setFollowing(List<String> following) {
        this.following = following
    }
}