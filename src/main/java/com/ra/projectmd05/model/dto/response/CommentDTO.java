package com.ra.projectmd05.model.dto.response;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDate createdAt;
    private Long parentId;
    private Long postId;
    private Long userId;
    private Boolean status;
    private Long childCommentCount; // Số lượng bình luận con
    private String avatarUrlUser; // ảnh đại diện người dùng
    private String userName;
}
