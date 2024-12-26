package com.ra.projectmd05.controller.user.group;

import com.ra.projectmd05.model.entity.Group;
import com.ra.projectmd05.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/groups")
public class GroupController {

    final private GroupService groupService;

    @PostMapping()
    public ResponseEntity<?> reactionPost(@RequestParam("groupName") String groupName) throws IOException {
        Group newGroup = groupService.createGroup(groupName);
        if (newGroup != null) {
            return new ResponseEntity<>("Tạo nhóm thành công.",HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Tạo nhóm thất bại.", HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupById(@PathVariable Long groupId) {
        Group group = groupService.getGroupByGroupId(groupId);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return new ResponseEntity<>("Nhóm không tồn tại.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) throws IOException {
        groupService.deleteGroup(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
