package org.example.uberprojectauthservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.services.JwtService;
import org.example.uberprojectauthservice.services.UserDetailsServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    // inside your class

    private final RequestMatcher uriMatcher = 
    new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/auth/validate", HttpMethod.GET.name());


    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("JwtToken")) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("Incoming token " + token);
        String email = jwtService.extractEmail(token);
        System.out.println("Email " + email);

        if(email != null){
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            if(jwtService.validateToken(token , userDetails.getUsername())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails , null , null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        System.out.println("Forwading Req");
        filterChain.doFilter(request , response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        return matcher.matches(request);
    }
}