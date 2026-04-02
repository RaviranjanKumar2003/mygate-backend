package com.example.demo.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // 🔹 List of public endpoints (bypass JWT)
    private static final String[] PUBLIC_URLS = {
            "/api/auth",
            "/api/societies",
            "/api/society-admins",
            "/api/notices",
            "/api/users",
            "/api/floors",
            "/api/flats",
            "/api/staff",
            "/api/file"
    };

    // 🔹 Helper: check if request matches a public endpoint
    private boolean isPublicUrl(String path) {
        for (String url : PUBLIC_URLS) {
            if (path.equals(url) || path.startsWith(url + "/")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ bypass public APIs
        if (isPublicUrl(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;


        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);

                // 🔥 risky line wrapped in try-catch
                username = jwtTokenHelper.getUsernameFromToken(token);
            }
        } catch (Exception e) {
            System.out.println("JWT Error: " + e.getMessage());
        }

        // ✅ safe authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}























//package com.example.demo.Configuration;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Autowired
//    private JwtTokenHelper jwtTokenHelper;
//
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        //  Authorization header uthao
//        String header = request.getHeader("Authorization");
//
//        String username = null;
//        String token = null;
//
//        //  Bearer token check
//        if (header != null && header.startsWith("Bearer ")) {
//            token = header.substring(7);
//            username = jwtTokenHelper.getUsernameFromToken(token);
//        }
//
//        //  User authenticate
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails =
//                    this.userDetailsService.loadUserByUsername(username);
//
//            if (jwtTokenHelper.validateToken(token, userDetails)) {
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
