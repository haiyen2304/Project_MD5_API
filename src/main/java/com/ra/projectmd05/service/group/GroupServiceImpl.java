package com.ra.projectmd05.service.group;
import com.ra.projectmd05.exception.DataExistException;
import com.ra.projectmd05.model.dto.response.GroupResponseDTO;
import com.ra.projectmd05.model.dto.response.UserResponseDTO;
import com.ra.projectmd05.model.entity.Group;
import com.ra.projectmd05.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group createGroup(String groupName) {
        return null;
    }

    @Override
    public void deleteGroup(Long groupId) {

    }

    @Override
    public Group getGroupByGroupId(Long groupId) {
        return null;
    }

    @Override
    public Page<GroupResponseDTO> getAllGroups(Pageable pageable) {
        Page<Group> groups = groupRepository.findAll(pageable); // Lấy danh sách nhóm từ repository

        return groups.map(this::convertToGroupResponseDTO);
    }

    @Override
    public Page<GroupResponseDTO> searchGroups(String keyword, Pageable pageable) {
//        Page<Group> groups = groupRepository.findByTitleContainingIgnoreCase(keyword, pageable); // Tìm kiếm nhóm theo từ khóa

//        return groups.map(this::convertToGroupResponseDTO);
        return null;
    }

    // Khóa nhóm
    public Group lockGroup(Long groupId) throws DataExistException {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (Boolean.TRUE.equals(group.getIsLocked())) {
            throw new DataExistException("Group is already locked");
        }

        group.setIsLocked(true);
        groupRepository.save(group);
        return group;
    }

    // Mở khóa nhóm
    public Group unlockGroup(Long groupId) throws DataExistException {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (Boolean.FALSE.equals(group.getIsLocked())) {
            throw new DataExistException("Group is already unlocked");
        }

        group.setIsLocked(false);
        groupRepository.save(group);
        return group;
    }

    public GroupResponseDTO convertToGroupResponseDTO(Group group) {
        return GroupResponseDTO.builder()
                .id(group.getId())
                .user(UserResponseDTO.builder()
                        .id(group.getUser().getId())
                        .firstName(group.getUser().getFirstName())
                        .lastName(group.getUser().getLastName())
                        .email(group.getUser().getEmail())
                        .phone(group.getUser().getPhone())
                        .birthDay(group.getUser().getBirthDay())
                        .gender(group.getUser().getGender().toString())
                        .status(group.getUser().getStatus().toString())
                        .build())
                .createdAt(group.getCreatedAt())
                .title(group.getTitle())
                .isLocked(group.getIsLocked())
                .type(group.getType())
                .build();
    }
}
