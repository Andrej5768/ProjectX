package com.andrewcorp.projectx.web.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class UserDTO implements Serializable {
    @NotNull
    private String id
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    private String username
    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 6, max = 40)
    private String fullName
    private List<String> followers
    private List<String> following

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
