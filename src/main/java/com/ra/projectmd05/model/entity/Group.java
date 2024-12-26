package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.constants.GroupType;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "my_groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Group extends BaseObject
{
    @Column(name = "title")
    private String title;

    @Builder.Default
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GroupType type = GroupType.PUBLIC;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

}
