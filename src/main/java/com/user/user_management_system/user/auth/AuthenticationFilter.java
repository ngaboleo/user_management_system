package com.user.user_management_system.user.auth;

import com.user.user_management_system.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  UserImplDetailService userImplDetailService;
    @Autowired
    private TokenUtil tokenUtil;

    public static final String TOKEN_PREFIX = "Bearer ";

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // get token
        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)){
            token = requestTokenHeader.substring(7);

            try {
                username = tokenUtil.getUsernameFromToken(token);
            }catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException
                    | IllegalArgumentException ex){
                logger.info(ex.getMessage());
            }
        }

        // validate token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userImplDetailService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set
            // authentication
            boolean tokenIsValid = tokenUtil.validateToken(token, userDetails);
            if (userDetails.isEnabled() && tokenIsValid) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
