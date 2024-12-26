package com.ra.projectmd05.model.dto.response;

import com.ra.projectmd05.constants.PostType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostResponseDTO {
    private Long id;                      // ID bài viết
    private String content;               // Nội dung bài viết
    private LocalDateTime createdAt;      // Thời gian tạo bài viết
    private LocalDateTime updatedAt;      // Thời gian cập nhật bài viết
    private PostType privacy;              // Quyền riêng tư
    private Long userId;                  // ID người đăng bài
    private String userName;              // Tên người đăng bài
    private String avatarUrl;             // URL ảnh đại diện người đăng bài
    private List<ImageResponseDTO> images; // Danh sách ảnh
    private List<TaggedUserResponseDTO> taggedUsers; // Danh sách người được gắn thẻ

}
