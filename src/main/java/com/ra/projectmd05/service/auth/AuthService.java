package com.ra.projectmd05.service.auth;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.UserLoginRequestDTO;
import com.ra.projectmd05.model.dto.request.UserRegisterRequestDTO;
import com.ra.projectmd05.model.dto.request.VerifyUserDTO;
import com.ra.projectmd05.model.dto.response.UserLoginResponseDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.User;

import java.util.List;

public interface AuthService {
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);
    void register(UserRegisterRequestDTO userRegisterRequestDTO) throws DataExistException;

    User userRegisterRequestDTOConvertToUser  (UserRegisterRequestDTO dto);
    UserRegisterResponseDTO userConvertToResponseDTO(User user);

    // Gửi email xác thực
    void sendVerificationEmail(User user);
    //gửi lại email xác thực khi token hết hạn
    void resendVerificationEmail(String email);
    boolean verifyUserCode(VerifyUserDTO verifyUserDTO);
    void updateEmail(Long userId, String newEmail);

    List<UserRegisterResponseDTO> findAll();


}
