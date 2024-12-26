package com.ra.projectmd05.model.dto.request;

import com.ra.projectmd05.constants.GroupRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChangeRoleRequestDTO {

    @NotBlank(message = "vui lòng nhập groupId")
    private Long groupId;
    @NotBlank(message = "vui lòng nhập targetMemberId")
    private Long targetMemberId;
    @NotBlank(message = "vui lòng nhập role")
    private GroupRole role;


}
