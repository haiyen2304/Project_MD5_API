package com.ra.projectmd05.model.dto.request;

import com.ra.projectmd05.constants.Gender;
import com.ra.projectmd05.constants.UserStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserDetailDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private UserStatus status;


}
