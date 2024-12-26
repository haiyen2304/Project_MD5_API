package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "comment_like")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentLike extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "icon_like_id")
    private IconLike icon;
}
