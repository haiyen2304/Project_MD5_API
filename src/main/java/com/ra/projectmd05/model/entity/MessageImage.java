package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_image")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MessageImage extends BaseObject
{
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
