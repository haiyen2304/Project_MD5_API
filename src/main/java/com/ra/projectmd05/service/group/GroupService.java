package com.ra.projectmd05.service.group;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.response.GroupResponseDTO;
import com.ra.projectmd05.model.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Group createGroup(String groupName);
    void deleteGroup(Long groupId);
    Group getGroupByGroupId(Long groupId);
    Page<GroupResponseDTO> getAllGroups(Pageable pageable);

    Page<GroupResponseDTO> searchGroups(String keyword, Pageable pageable);

    Group lockGroup(Long groupId) throws DataExistException;
    Group unlockGroup(Long groupId) throws DataExistException;

    GroupResponseDTO convertToGroupResponseDTO(Group group);
}
