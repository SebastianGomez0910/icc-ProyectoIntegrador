package ec.edu.ups.icc.proyectointegrador.security.filter;

import java.io.IOException;
import java.time.Duration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ec.edu.ups.icc.proyectointegrador.security.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitingService rateLimitingService;

    public RateLimitingFilter(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        String clientIp = getClientIp(request);
        
        boolean allowed = true;
        String operationKey = "";
        long maxLimit = 0;
        Duration windowTime = Duration.ZERO;

         if (path.contains("/auth/register")) {
            operationKey = "register";
            maxLimit = 3;
            windowTime = Duration.ofHours(1); //cambiar a una hhora
            allowed = rateLimitingService.isAllowed(operationKey, clientIp, maxLimit, windowTime);
        } 
        
        else if (path.contains("/reports")) {
            String username = getAuthenticatedUsername();
            operationKey = "reports";
            maxLimit = 5;
            windowTime = Duration.ofMinutes(1);
            allowed = rateLimitingService.isAllowed(operationKey, username != null ? username : clientIp, maxLimit, windowTime);
        } 
       
        else if (path.contains("/public")) {
            operationKey = "public";
            maxLimit = 60;
            windowTime = Duration.ofMinutes(1);
            allowed = rateLimitingService.isAllowed(operationKey, clientIp, maxLimit, windowTime);
        } 
        
        else {
            String username = getAuthenticatedUsername();
            operationKey = "authenticated";
            maxLimit = 120;
            windowTime = Duration.ofMinutes(1);
            allowed = rateLimitingService.isAllowed(operationKey, username != null ? username : clientIp, maxLimit, windowTime);
        }

        
        if (!allowed) {
            response.setStatus(429);
            response.setHeader("Retry-After", "60");
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Too Many Requests\", \"message\": \"Has superado el límite de solicitudes permitidas para esta operación.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            return authentication.getName();
        }
        return null;
    }
}
