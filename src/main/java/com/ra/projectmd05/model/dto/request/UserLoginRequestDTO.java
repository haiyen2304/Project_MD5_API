package com.ra.projectmd05.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginRequestDTO {
    @NotBlank(message = "hãy điền email")
    private String email;
    @NotBlank(message = "hãy điền password")
    private String password;
}
