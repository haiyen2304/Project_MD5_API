package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.constants.RoleName;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role extends BaseObject
{
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
