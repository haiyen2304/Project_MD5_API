package com.ra.projectmd05.security.jwt;

import com.ra.projectmd05.security.UserDetailService;
import com.ra.projectmd05.service.Token.TokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailService userDetailService;
    private final TokenServiceImpl tokenServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lấy token từ request
        String token = getTokenFromRequest(request);
        try {
            // Kiểm tra token có hợp lệ và không nằm trong danh sách "blacklist"
            if (token != null && jwtProvider.validateToken(token) &&  !tokenServiceImpl.isTokenInvalidated(token)) {
                // Lấy tên người dùng từ token
                String userName = jwtProvider.getUserNameFromToken(token);
                // Tải thông tin người dùng từ cơ sở dữ liệu hoặc nơi lưu trữ
                UserDetails userDetails = userDetailService.loadUserByUsername(userName);
                if (userDetails != null) {
                    // Tạo đối tượng xác thực với thông tin người dùng và các quyền
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // Thiết lập chi tiết bổ sung cho xác thực
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Đặt thông tin xác thực vào SecurityContext của Spring
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception exception) {
            // Ghi lại thông tin lỗi nếu có ngoại lệ xảy ra
            logger.error(exception.getMessage());
        }
        // Tiếp tục xử lý các filter còn lại trong chuỗi
        filterChain.doFilter(request, response);
    }

    // lay token guir len tu request
    public String getTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}

//Phần 3: JwtAuthTokenFilter
//Mục đích:
//Xử lý filter để xác thực người dùng dựa trên JWT. Đây là bước trung gian giữa request của người dùng và hệ thống bảo mật của Spring Security.
//
//Chức năng chính:
//
//Lấy token từ request (getTokenFromRequest):
//
//Trích xuất token từ header Authorization, kiểm tra tiền tố "Bearer ".
//Xử lý filter (doFilterInternal):
//
//Kiểm tra token có hợp lệ và không nằm trong danh sách blacklist (dùng TokenServiceImpl).
//Nếu hợp lệ:
//Lấy username từ token.
//Truy xuất thông tin người dùng từ UserDetailService.
//Tạo đối tượng xác thực (UsernamePasswordAuthenticationToken) chứa thông tin người dùng và quyền.
//Đặt thông tin này vào SecurityContextHolder để Spring Security sử dụng trong quá trình xử lý request.
//Tiếp tục xử lý chuỗi filter:
//
//Gọi filterChain.doFilter(request, response) để các filter khác tiếp tục xử lý request.
