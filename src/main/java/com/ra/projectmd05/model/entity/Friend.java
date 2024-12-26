package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.constants.FriendshipStatus;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Friend extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Người gửi yêu cầu kết bạn

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friendUser;// Người nhận yêu cầu kết bạn

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FriendshipStatus status; // Trạng thái mối quan hệ bạn bè: PENDING, ACCEPTED, DECLINED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian gửi yêu cầu kết bạn

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian cập nhật trạng thái mối quan hệ


}
