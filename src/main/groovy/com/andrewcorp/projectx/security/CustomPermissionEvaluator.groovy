package com.andrewcorp.projectx.security

import com.andrewcorp.projectx.web.dto.PostDTO;
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.access.PermissionEvaluator
import org.springframework.stereotype.Component
import java.io.Serializable
/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Component
class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false
    }

    @Override
    boolean hasPermission(
        Authentication authentication,
        Serializable targetId,
        String targetType,
        Object permission
    ) {
        if ("userId" == permission) {
            String userIdInContext = authentication.getCredentials().toString()

            return targetType == userIdInContext
        }
        return false
    }
}