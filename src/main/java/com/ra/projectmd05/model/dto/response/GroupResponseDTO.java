package com.ra.projectmd05.model.dto.response;
import com.ra.projectmd05.constants.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponseDTO {
    private Long id;
    private String title;

    private GroupType type = GroupType.PUBLIC;

    private LocalDate createdAt = LocalDate.now();

    private UserResponseDTO user;

    private Boolean isLocked = false;

}
