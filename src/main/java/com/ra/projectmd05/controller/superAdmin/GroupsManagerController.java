package com.ra.projectmd05.controller.superAdmin;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.response.GroupResponseDTO;
import com.ra.projectmd05.model.entity.Group;

import com.ra.projectmd05.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/superAdmin/groups")
public class GroupsManagerController {

    @Autowired
    private GroupService groupService;

    // Hiển thị danh sách nhóm
    @GetMapping
    public ResponseEntity<Page<GroupResponseDTO>> listGroups(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDTO> groups = groupService.getAllGroups(pageable);
        return ResponseEntity.ok(groups);
    }

    // Tìm kiếm nhóm theo tiêu đề
    @GetMapping("/search")
    public ResponseEntity<Page<GroupResponseDTO>> searchGroups(@RequestParam String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupResponseDTO> groups = groupService.searchGroups(keyword, pageable);
        return ResponseEntity.ok(groups);
    }

    // Khóa nhóm
    @PutMapping("/{groupId}/lock")
    public ResponseEntity<GroupResponseDTO> lockGroup(@PathVariable Long groupId) throws DataExistException {
        // Gọi service để khóa nhóm
        Group group = groupService.lockGroup(groupId);

        // Chuyển đổi Group sang GroupResponseDTO
        GroupResponseDTO responseDTO = groupService.convertToGroupResponseDTO(group);

        // Trả về GroupResponseDTO
        return ResponseEntity.ok(responseDTO);
    }

    // Mở khóa nhóm
    @PutMapping("/{groupId}/unlock")
    public ResponseEntity<GroupResponseDTO> unlockGroup(@PathVariable Long groupId) throws DataExistException {
        // Gọi service để mở khóa nhóm
        Group group = groupService.unlockGroup(groupId);

        // Chuyển đổi Group sang GroupResponseDTO
        GroupResponseDTO responseDTO = groupService.convertToGroupResponseDTO(group);

        // Trả về GroupResponseDTO
        return ResponseEntity.ok(responseDTO);
    }

}
