package com.ra.projectmd05.controller.auth;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.UserLoginRequestDTO;
import com.ra.projectmd05.model.dto.request.UserRegisterRequestDTO;
import com.ra.projectmd05.model.dto.request.VerifyUserDTO;
import com.ra.projectmd05.model.dto.response.UserLoginResponseDTO;
import com.ra.projectmd05.service.Token.TokenService;
import com.ra.projectmd05.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO userLoginRequestDTO){
        return new ResponseEntity<>(authService.login(userLoginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) throws DataExistException {
        authService.register(userRegisterRequestDTO);
        return new ResponseEntity<>("đã gửi mã xác thực vào email"+ userRegisterRequestDTO.getEmail(),HttpStatus.CREATED);
    }

    // API cho phép người dùng yêu cầu gửi lại mã xác thực
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationCode(@RequestParam String email) {
        try {
            // Gửi lại mã xác thực nếu mã đã hết hạn
            authService.resendVerificationEmail(email);
            return ResponseEntity.ok("Mã xác thực mới đã được gửi đến email của bạn.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API nhận email và mã xác thực từ người dùng
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO) throws DataExistException {
        try {
            // Gọi service để xác thực mã
            boolean isVerified = authService.verifyUserCode(verifyUserDTO);
            if (isVerified) {
                return ResponseEntity.status(HttpStatus.OK).body("Xác thực thành công! Tài khoản đã được kích hoạt.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã xác thực không chính xác.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi xác thực mã: " + e.getMessage());
        }
    }

    // Phương thức cập nhật email cho người dùng
    @PutMapping("/update-email/{userId}")
    public ResponseEntity<String> updateEmail(@PathVariable Long userId, @RequestParam String newEmail) {
        try {
            // Gọi service để cập nhật email và gửi lại mã xác thực
            authService.updateEmail(userId, newEmail);
            return ResponseEntity.ok("Email đã được cập nhật thành công và mã xác thực đã được gửi lại.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}



