package com.ra.projectmd05.security.jwt;

import com.ra.projectmd05.security.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.util.Date;
//JwtProvider: Quản lý token (tạo, kiểm tra, giải mã).
//JwtEntryPoint: Xử lý lỗi khi xác thực thất bại.
//JwtAuthTokenFilter: Kiểm tra token trong request, xác thực và thiết lập ngữ cảnh bảo mật cho người dùng.
@Component
public class JwtProvider {
    @Value("${expired}")
    private Long EXPIRED;
    @Value("${secret_key}")
    private String SECRET_KEY;
    private Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    public String generateToken(UserPrinciple userPrinciple){
        // Tạo ra thời gian sống của token
        Date dateExpiration = new Date(new Date().getTime() + EXPIRED);

        return Jwts.builder()
                .setSubject(userPrinciple.getUsername()).
                setExpiration(dateExpiration).
                signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
    }
    public Boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpressionException |SignatureException | ExpiredJwtException | MalformedJwtException exception){
            logger.error("Exception Authentication {}",exception.getMessage());
        }
        return false;
    }
    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
//Mục đích:
//Lớp này chịu trách nhiệm tạo, xác minh tính hợp lệ, và giải mã JWT.
//
//Chức năng chính:
//
//Tạo JWT (generateToken):
//
//Tạo một token có thời gian sống dựa trên EXPIRED (lấy từ file cấu hình) và SECRET_KEY.
//Sử dụng Jwts.builder() để tạo token với thông tin username, thời gian hết hạn, và ký mã hóa với thuật toán HS512.
//Xác minh token (validateToken):
//
//Kiểm tra token có hợp lệ không bằng cách giải mã và so khớp với SECRET_KEY.
//Ghi log nếu token bị lỗi, hết hạn, hoặc không đúng định dạng.
//Lấy tên người dùng từ token (getUserNameFromToken):
//
//Giải mã token để lấy ra Subject (username) từ payload.
