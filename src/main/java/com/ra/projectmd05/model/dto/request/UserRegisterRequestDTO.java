package com.ra.projectmd05.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.projectmd05.constants.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserRegisterRequestDTO {

    @NotBlank(message = "First name không được để trống")
    private String firstName;

    @NotBlank(message = "Last name không được để trống")
    private String lastName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được dài quá 100 ký tự")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password phải chứa ít nhất một chữ hoa, một chữ thường, một số và một ký tự đặc biệt"
    )
    private String password;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(\\+84|0)[3|5|7|8|9]\\d{8}$",
            message = "Số điện thoại không đúng định dạng Việt Nam"
    )
    private String phone;
    @JsonFormat(pattern = "dd/mm/yyyy")
    private Date birthDay; // Ngày sinh của người dùng
    private Gender gender;
}
