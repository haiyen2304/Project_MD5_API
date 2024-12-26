package com.ra.projectmd05.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ra.projectmd05.constants.Gender;
import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseObject
{
    @Column(name="first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    private Date birthDay;
    private boolean isLogin;
    private Gender gender;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnoreProperties({"user"})
//    private List<Avatar> avatar;



    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.VERIFY;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    // Thêm để lưu trữ thời gian code xác thực
    private LocalDateTime verifyCodeDate;  // Ngày tạo token
    @JsonIgnore
    private String verifyCode;    // code xác thực


}
