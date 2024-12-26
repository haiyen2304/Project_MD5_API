package com.ra.projectmd05.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StoryRequestDTO {

    @NotBlank(message = "hãy nhập mediaType")
    private String mediaType; // "text", "image", "video"
    private MultipartFile mediaUrl; // Đường dẫn của ảnh/video (nếu có)
    private String content; // Nội dung văn bản (nếu có)
}
