## ProjectX - Twitter-like API
ProjectX is a Twitter-like API built with Groovy and Spring Boot, using MongoDB as the database. It provides users with the ability to register, log in, log out, create posts, view their feed, view other users' feeds, leave comments, like and unlike posts, and follow and unfollow other users.

### Table of Contents
- [Features](#Features)
- [Prerequisites](#Prerequisites)
- [Getting Started](#Getting-Started)
- [API Endpoints](#API-Endpoints)
- [Testing](#Testing)
- [Docker](#Docker)


### Features
- User registration and authentication
- Create, edit, and delete posts
- Like and unlike posts
- Follow and unfollow other users
- Comment on posts
- View user feeds, including posts, likes, and comments

### Prerequisites
Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 17 or higher
- Docker (for containerization)
- MongoDB installed and running

### Getting Started
To get started with ProjectX, follow these steps:

Clone the repository:


``` bash
git clone https://github.com/your-username/projectx.git
```

Set the environment for connect to MongoDb in the `application.properties` file



Navigate to the project directory:
``` bash
cd projectx
```
Build the application using Gradle:

``` bash
./gradlew build
```
Run the application with Docker Compose:

``` bash
java -jar projectX.jar
```

Access the API at http://localhost:8080/api

### API Endpoints
- User
  - POST /api/users/register: Register a new user.
  - POST /api/users/login: Log in as a user.
  - POST /api/users/logout: Log out as a user.
  - GET /api/users/{userId}: Get user information.
  - PUT /api/users/{userId}: Update user information.
  - DELETE /api/users/{userId}: Delete a user.

- Post
  - POST /api/posts/create: Create a new post.
  - PUT /api/posts/update: Update a post.
  - DELETE /api/posts/{postId}/delete: Delete a post.
  - GET /api/posts/{postId}: Get a post.
  - GET /api/posts/user/{userId}: Get posts by a user.
  - GET /api/posts/feed: Get a user's feed.
  - GET /api/posts/feed/{userId}: Get another user's feed.

- Like
  - POST /api/posts/{postId}/like: Like a post.
  - POST /api/posts/{postId}/unlike: Unlike a post.
  
- Subscription
  - POST /api/subscriptions/subscribe: Subscribe to a user.
  - POST /api/subscriptions/unsubscribe: Unsubscribe from a user.
  
- Comment
  - POST /api/posts/comment: Create a comment for post.
  - GET /api/posts/comments: Get all comments for the post.

### Testing
ProjectX uses Spock for testing. You can run the tests using Gradle:

```bash
./gradlew test
```
But before you need to rut the MongoDB in your local machine or in a container:

```bash
docker run -d -p 27017:27017 mongo
```

or
```bash
docker compose -f docker-compose-test.yaml up   
```

### Docker
You can use Docker Compose to run the application and MongoDB in containers. Make sure you have Docker and Docker Compose installed.

Build image and start the containers:

```bash
docker compose up
```
The application will be accessible at http://localhost:8080.