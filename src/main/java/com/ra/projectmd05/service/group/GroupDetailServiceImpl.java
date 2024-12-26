package com.ra.projectmd05.service.group;

import com.ra.projectmd05.constants.GroupRole;
import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.request.GroupChangeRoleRequestDTO;
import com.ra.projectmd05.model.entity.Group;
import com.ra.projectmd05.model.entity.GroupDetail;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.GroupDetailRepository;
import com.ra.projectmd05.repository.GroupRepository;
import com.ra.projectmd05.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupDetailServiceImpl implements GroupDetailService {
    private final UserServiceImpl userServiceImpl;
    private final GroupRepository groupRepository;
    private final GroupDetailRepository groupDetailRepository;
    @Override
    public GroupDetail getGroupDetail(Long groupId) {
        return null;
    }

    @Override
    public Long getMemberCountByGroupId(Long groupId) {
        return groupDetailRepository.countMembersByGroupId(groupId);
    }


    @Transactional
    @Override
    public void leaveGroup(Long groupId) {
        User user = userServiceImpl.getCurrentUserInfo();
        groupDetailRepository.leaveGroup(groupId, user.getId());

    }

    @Override
    public GroupDetail joinGroup(Long groupId) throws DataExistException {
        User user = userServiceImpl.getCurrentUserInfo();
        Group group = groupRepository.findById(groupId).orElse(null);

        if (groupDetailRepository.existsByGroupIdAndMember_Id(groupId, user.getId())) {
            throw new DataExistException("Đã tham gia group");
        }

        if (group != null && user != null) {
            GroupDetail groupDetail = new GroupDetail();
            groupDetail.setGroup(group);
            groupDetail.setJoinDate(LocalDate.now());
            groupDetail.setMember(user);
            groupDetail.setRole(GroupRole.MEMBER);
            return groupDetailRepository.save(groupDetail);
        }

        return null;
    }

    @Override
    public GroupDetail changeRole(GroupChangeRoleRequestDTO groupChangeRoleRequestDTO) throws  AccessDeniedException {
        User user = userServiceImpl.getCurrentUserInfo();
        System.out.println("isAdminIsTrator: " + isAdminIsTrator(groupChangeRoleRequestDTO.getGroupId(), user.getId()));
        System.out.println("isCollaborator: " + isCollaborator(groupChangeRoleRequestDTO.getGroupId(), user.getId()));

        if (!isAdminIsTrator(groupChangeRoleRequestDTO.getGroupId(), user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền thay đổi role trong nhóm");
        }

        Optional<GroupDetail> groupDetail = groupDetailRepository.findByGroupIdAndMemberId(groupChangeRoleRequestDTO.getGroupId(),groupChangeRoleRequestDTO.getTargetMemberId());

        if (groupDetail.isPresent()) {
            groupDetail.get().setRole(groupChangeRoleRequestDTO.getRole());
            return groupDetailRepository.save(groupDetail.get());
        }
        return null;
    }


    public boolean isAdminIsTrator(Long groupId, Long userId) {
       return groupDetailRepository.existsByGroupIdAndMember_IdAndRole(groupId,userId,GroupRole.ADMINISTRATOR);
    }


    public boolean isCollaborator(Long groupId, Long userId) {
        return groupDetailRepository.existsByGroupIdAndMember_IdAndRole(groupId,userId,GroupRole.COLLABORATOR);
    }


}
