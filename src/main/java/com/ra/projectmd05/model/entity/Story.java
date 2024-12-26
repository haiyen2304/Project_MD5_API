package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Story extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "media_type", nullable = false)
    private String mediaType; // "text", "image", "video"
    @Column(name = "media_url", nullable = true)
    private String mediaUrl; // Đường dẫn của ảnh/video (nếu có)
    @Column(name = "content", length = 500, nullable = true)
    private String content; // Nội dung văn bản (nếu có)\
    @Column(name = "created_At")
    private LocalDateTime createdAt;
    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt; // Thời gian hết hạn (ví dụ: 24 giờ)
}