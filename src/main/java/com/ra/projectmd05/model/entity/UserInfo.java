package com.ra.projectmd05.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo extends BaseObject
{
    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "wallpaper")
    private String wallpaper;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "last_login")
    private LocalDate lastLogin;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
