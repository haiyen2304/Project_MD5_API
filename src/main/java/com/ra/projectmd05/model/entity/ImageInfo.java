package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImageInfo extends BaseObject
{
    @Column(name = "url")
    private String url;

    @Column(name = "is_avatar")
    private Boolean isAvatar;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private UserInfo info;
}
