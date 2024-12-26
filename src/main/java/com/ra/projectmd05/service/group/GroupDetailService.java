package com.ra.projectmd05.service.group;

import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.GroupChangeRoleRequestDTO;
import com.ra.projectmd05.model.entity.GroupDetail;

import java.nio.file.AccessDeniedException;

public interface GroupDetailService {
    GroupDetail getGroupDetail(Long groupId);
    GroupDetail joinGroup(Long groupId) throws DataExistException;
    Long getMemberCountByGroupId(Long groupId);
    void leaveGroup(Long groupId) ;

    public GroupDetail changeRole(GroupChangeRoleRequestDTO groupChangeRoleRequestDTO) throws  AccessDeniedException;
}
