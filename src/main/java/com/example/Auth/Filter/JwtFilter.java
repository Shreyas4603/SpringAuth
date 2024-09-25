package com.example.Auth.Filter;

import com.example.Auth.Models.User;
import com.example.Auth.Repositories.UserRepository;
import com.example.Auth.Services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Check if the Authorization header contains a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            email = jwtService.extractEmail(token); // Extract email from token
        }



        // Check if email is present and the user is not yet authenticated in the SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Attempting authentication for user: " + email);

            // Fetch the user by email from the database
            Optional<User> userOpt = userRepository.findByEmail(email);

            // Validate the token and check if the user exists
            if (userOpt.isPresent() && jwtService.validateToken(token, userOpt.get())) {
                User user = userOpt.get();

                // Create an authentication object without authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), null);
//                System.out.println("AUthorized  authentication for user: " + email + " "+authentication.isAuthenticated());


                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                System.out.println("Token validation failed or user not found.");
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
