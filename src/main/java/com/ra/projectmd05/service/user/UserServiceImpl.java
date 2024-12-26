package com.ra.projectmd05.service.user;

import com.ra.projectmd05.constants.RoleName;
import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.exception.UnauthorizedAccessException;
import com.ra.projectmd05.model.dto.request.UserDetailDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.UserRepository;
import com.ra.projectmd05.security.UserPrinciple;
import com.ra.projectmd05.service.friend.FriendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public User getCurrentUserInfo() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userPrinciple.getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại"));
    }

    @Override
    public List<User> searchUsersByName(String userName) {
        User currentUser = getCurrentUserInfo();
       List<User> userList= userRepository.findByUserNameContainingIgnoreCase(userName);
        return userList.stream().filter(u-> !Objects.equals(u.getId(), currentUser.getId())).toList();
    }

    @Override
    public User getUserByIdProfile(Long id) {
       return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"không tìm thấy người dùng"));
    }

    //-------------------N start---------------------

    public Page<UserDetailDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(this::convertToDTO);
    }

    // Chi tiết người dùng
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Khóa tài khoản
    public void lockAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng với ID: " + id));

        // Kiểm tra nếu người dùng có vai trò ROLE_SUPER_ADMIN thì không cho phép khóa
        boolean isSuperAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.ROLE_SUPER_ADMIN);

        if (isSuperAdmin) {
            throw new UnauthorizedAccessException("Không thể khóa tài khoản với vai trò ROLE_SUPER_ADMIN.");
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new IllegalStateException("Tài khoản với ID " + id + " đã bị khóa trước đó.");
        }

        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    // Mở tài khoản
    public void unlockAccount(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        });
    }

    // Tìm kiếm người dùng
    public List<UserDetailDTO> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    // Sắp xếp người dùng theo tên
    public List<UserDetailDTO> sortUsersByFirstName(String sortOrder) {
        Comparator<User> comparator = (sortOrder.equalsIgnoreCase("asc")) ?
                (u1, u2) -> u1.getFirstName().compareToIgnoreCase(u2.getFirstName()) :
                (u1, u2) -> u2.getFirstName().compareToIgnoreCase(u1.getFirstName());

        return userRepository.findAll().stream()
                .sorted(comparator)
                .map(user -> UserDetailDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .status(user.getStatus())
                        .gender(user.getGender())
                        .build())
                .toList();
    }

    private UserDetailDTO convertToDTO(User user) {
        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setGender(user.getGender());
        return dto;
    }
}