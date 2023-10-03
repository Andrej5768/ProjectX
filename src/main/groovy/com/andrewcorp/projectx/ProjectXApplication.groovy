package com.andrewcorp.projectx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
class ProjectXApplication {

	static void main(String[] args) {
		SpringApplication.run(ProjectXApplication, args)
	}
}
