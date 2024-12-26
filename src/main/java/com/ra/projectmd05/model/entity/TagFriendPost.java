package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tag_friend_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TagFriendPost extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Bài đăng có người được gắn thẻ (Khóa ngoại từ bảng posts)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User taggedUser; // Người được gắn thẻ (Khóa ngoại từ bảng users)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian gắn thẻ bạn bè vào bài đăng

    @ManyToOne
    @JoinColumn(name = "tagged_by", nullable = false)
    private User taggedBy; // Người thực hiện gắn thẻ (Khóa ngoại từ bảng users)


}
