package com.ra.projectmd05.model.dto.response;

import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.model.entity.Role;
import com.ra.projectmd05.model.entity.UserInfo;
import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserRegisterResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private UserStatus status;
    private Set<Role> roles;
    private UserInfo info;
    private SuggestedFriendDTO suggestedFriendDTO;
}
