package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_image")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentImage extends BaseObject
{
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
