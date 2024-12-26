package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "room_participants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomParticipants extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Column(name = "join_date")
    private LocalDate joinDate = LocalDate.now();
}
