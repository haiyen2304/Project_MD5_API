package com.ra.projectmd05.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Un authentication");
        logger.error("Un Authentication {}",authException.getMessage());
    }
}
//JwtEntryPoint
//Mục đích:
//Xử lý lỗi xác thực (Authentication) khi người dùng truy cập vào tài nguyên cần bảo vệ mà không cung cấp hoặc cung cấp token không hợp lệ.
//
//Chức năng chính:
//
//Triển khai interface AuthenticationEntryPoint của Spring Security.
//Khi có lỗi xác thực, nó trả về mã HTTP 401 (Unauthorized) và thông báo lỗi “Un authentication”.
//Ghi log chi tiết lỗi xác thực.