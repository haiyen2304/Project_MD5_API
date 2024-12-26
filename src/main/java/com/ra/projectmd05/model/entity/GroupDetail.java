package com.ra.projectmd05.model.entity;


import com.ra.projectmd05.constants.GroupRole;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "group_detail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GroupDetail extends BaseObject
{
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private GroupRole role;

    @Column(name = "join_date")
    private LocalDate joinDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User member;
}
