package com.andrewcorp.projectx.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Component
class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JwtProvider jwtProvider;

    private static final String USER_CLAIM = "USER";

    JwtAuthorizationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        logger.trace("Authorization header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");

            boolean isTokenValid = jwtProvider.validateToken(token)
            logger.trace("isTokenValid: " + isTokenValid)
            if (isTokenValid) {
                Collection<GrantedAuthority> authorities = new ArrayList<>()
                String username = jwtProvider.getUsernameFromToken(token)
                authorities.add(new SimpleGrantedAuthority(USER_CLAIM))
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities)
                SecurityContextHolder.getContext().setAuthentication(authentication)
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
