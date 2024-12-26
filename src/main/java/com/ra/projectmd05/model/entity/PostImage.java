package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_image")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostImage extends BaseObject
{
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Bài đăng chứa hình ảnh

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian hình ảnh được thêm vào bài đăng

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary; // Hình ảnh chính của bài đăng

    @Column(name = "position", nullable = false)
    private Integer position; // Vị trí sắp xếp của hình ảnh trong bài đăng

    private String url;



}
