package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "followers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Follower extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
}
