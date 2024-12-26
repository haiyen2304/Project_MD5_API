package com.ra.projectmd05.controller.user.group;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.GroupChangeRoleRequestDTO;
import com.ra.projectmd05.model.entity.GroupDetail;
import com.ra.projectmd05.service.group.GroupDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/groupDetail")
public class GroupDetailController {
    private final GroupDetailService groupDetailService;

    @PostMapping("/join/{groupId}")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId) {
        try {
            GroupDetail groupDetail = groupDetailService.joinGroup(groupId);
            return new ResponseEntity<>(groupDetail, HttpStatus.CREATED);
        } catch (DataExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while joining the group.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/leave/{groupId}")
    public ResponseEntity<?> leaveGroup(@PathVariable Long groupId) {
        groupDetailService.leaveGroup(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/changeRole")
    public ResponseEntity<?> changeRole(@RequestBody GroupChangeRoleRequestDTO groupChangeRoleRequestDTO) throws AccessDeniedException {

        GroupDetail groupDetail = groupDetailService.changeRole(groupChangeRoleRequestDTO);
        if (groupDetail != null) {
            return new ResponseEntity<>(groupDetail, HttpStatus.OK);
        }else  {
            return new ResponseEntity<>("thay đổi quyền thất bại",HttpStatus.NOT_FOUND);
        }
    }
}
