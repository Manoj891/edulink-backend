package com.ms.ware.online.solution.school.config.security;


import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.exception.UnauthorizedException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@Service
@Slf4j
public class EndpointRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;


    @Override
    public void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");

        try {
            if (authToken != null && !authToken.isEmpty()) {
                if (!authToken.startsWith("Bearer ")) {
                    throw new UnauthorizedException();
                }
                String context = DatabaseName.getDocumentUrl();
                AuthenticatedUser user = jwtHelper.decodeToken(authToken);
                String method = request.getMethod();
                String uri = request.getRequestURI();
                if (!context.equalsIgnoreCase(user.getContext())) {
                    throw new CustomException("Invalid authorization");
                } else if (user.getRole().equalsIgnoreCase("STU")) {
                    if (!uri.contains("/api/panel/student/")) throw new UnauthorizedException();
                } else if (user.getRole().equalsIgnoreCase("TCR")) {
                    if (!uri.contains("/api/TeacherPanel/")) throw new UnauthorizedException();
                }
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, null));
                log.info("{} {} {}", method, uri, user.getUserName());
            }
            chain.doFilter(request, response);
        } catch (CustomException e) {
            response.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
            response.setStatus(900);
        }


    }
}
