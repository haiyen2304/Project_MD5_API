package com.ra.projectmd05.service.user;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.UserDetailDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getCurrentUserInfo();
    // tìm kiếm người dùng theo tên
     List<User> searchUsersByName(String userName);
    User getUserByIdProfile(Long id);



    //-------------------N start---------------------

    Page<UserDetailDTO> getAllUsers(int page, int size, String sortBy, String sortDir);
    Optional<User> getUserById(Long id);
    void lockAccount(Long id) throws DataExistException;
    void unlockAccount(Long id);
    List<UserDetailDTO> searchUsers(String keyword) throws DataExistException;
    List<UserDetailDTO> sortUsersByFirstName(String sortOrder);

}
