package com.andrewcorp.projectx.web.dto

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class PagedFeedDTO implements Serializable {
    List<PostDTO> posts
    List<LikeDTO> likes
    List<CommentDTO> comments
    int currentPage
    int pageSize
    long totalItems
    int totalPages

    PagedFeedDTO() {
    }

    List<PostDTO> getPosts() {
        return posts
    }

    void setPosts(List<PostDTO> posts) {
        this.posts = posts
    }

    List<LikeDTO> getLikes() {
        return likes
    }

    void setLikes(List<LikeDTO> likes) {
        this.likes = likes
    }

    List<CommentDTO> getComments() {
        return comments
    }

    void setComments(List<CommentDTO> comments) {
        this.comments = comments
    }

    int getCurrentPage() {
        return currentPage
    }

    void setCurrentPage(int currentPage) {
        this.currentPage = currentPage
    }

    int getPageSize() {
        return pageSize
    }

    void setPageSize(int pageSize) {
        this.pageSize = pageSize
    }

    long getTotalItems() {
        return totalItems
    }

    void setTotalItems(long totalItems) {
        this.totalItems = totalItems
    }

    int getTotalPages() {
        return totalPages
    }

    void setTotalPages(int totalPages) {
        this.totalPages = totalPages
    }
}
