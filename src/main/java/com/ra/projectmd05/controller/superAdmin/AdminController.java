package com.ra.projectmd05.controller.superAdmin;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.UserDetailDTO;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/superAdmin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Hiển thị danh sách người dùng
    @GetMapping
    public ResponseEntity<Page<UserDetailDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<UserDetailDTO> userPage = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(userPage);
    }

    // Chi tiết người dùng
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Khóa tài khoản
    @PutMapping("/{id}/lock")
    public ResponseEntity<String> lockAccount(@PathVariable Long id) throws DataExistException {
        userService.lockAccount(id);
        return ResponseEntity.ok().build();
    }

    // Mở tài khoản
    @PutMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockAccount(@PathVariable Long id) {
        userService.unlockAccount(id);
        return ResponseEntity.ok().build();
    }

    // Tìm kiếm người dùng
    @GetMapping("/search")
    public ResponseEntity<List<UserDetailDTO>> searchUsers(@RequestParam(defaultValue = "") String keyword) throws DataExistException {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    // Sắp xếp người dùng theo tên
    @GetMapping("/sort")
    public ResponseEntity<List<UserDetailDTO>> sortUsers(@RequestParam(defaultValue = "asc") String sortOrder) {
        // Gọi service với tham số sortOrder
        return ResponseEntity.ok(userService.sortUsersByFirstName(sortOrder));
    }
}
