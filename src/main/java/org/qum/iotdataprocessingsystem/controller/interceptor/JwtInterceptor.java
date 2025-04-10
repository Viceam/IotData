package org.qum.iotdataprocessingsystem.controller.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.qum.iotdataprocessingsystem.util.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果是预检请求，直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtils.parseToken(token);
                String username = claims.getSubject();
                String role = claims.get("role", String.class); // 获取用户角色
                request.setAttribute("userClaims", claims);
                request.setAttribute("username", username);
                request.setAttribute("role", role);
                return true;
            } catch (Exception e) {
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return false;
            }
        }
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
