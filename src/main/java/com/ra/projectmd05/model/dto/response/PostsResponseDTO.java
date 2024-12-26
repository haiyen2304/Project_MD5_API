package com.ra.projectmd05.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String location;
    private String postType;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private Boolean hasImage;
    private Boolean isHidden;
}
