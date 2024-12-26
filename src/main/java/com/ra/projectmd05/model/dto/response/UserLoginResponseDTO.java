package com.ra.projectmd05.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ra.projectmd05.model.entity.UserInfo;
import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginResponseDTO {
    private String userName;
    private String accessToken;
    private String typeToken;
    private Set<String> roles;
    @JsonIgnoreProperties({"user"})
    private UserInfo userInfo;
}
