package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.constants.Icon;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "icon_like")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IconLike extends BaseObject
{
    @Column(name = "icon")
    @Enumerated(EnumType.STRING)
    private Icon icon;
}
