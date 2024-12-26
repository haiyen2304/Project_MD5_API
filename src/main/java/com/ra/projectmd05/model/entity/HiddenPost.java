package com.ra.projectmd05.model.entity;

import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "hidden_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HiddenPost extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người dùng ẩn bài viết

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // Bài viết bị ẩn
}
