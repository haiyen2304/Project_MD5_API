package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.constants.PostType;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Liên kết với bảng users (ID người đăng bài)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String location;

    @Column(name = "is_public")
    private PostType postType;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "share_count")
    private Integer shareCount;

    @Column(name = "has_image")
    private Boolean hasImage;

    //-----------N start -------------
    @Column(name = "is_hidden")
    private Boolean hidden; // Đánh dấu bài viết bị ẩn
}
