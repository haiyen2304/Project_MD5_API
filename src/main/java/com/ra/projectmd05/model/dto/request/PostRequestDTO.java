package com.ra.projectmd05.model.dto.request;

import com.ra.projectmd05.constants.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostRequestDTO {

    @NotBlank(message = "Nội dung bài đăng không được để trống.")
    @Size(min = 1, max = 5000, message = "Nội dung bài đăng phải từ 1 đến 5000 ký tự.")
    private String content; // Nội dung bài đăng

    @NotNull(message = "Quyền riêng tư không được để trống.")
    @Builder.Default
    private PostType privacy = PostType.PUBLIC; // Quyền riêng tư: công khai hoặc chỉ bạn bè

    private List<MultipartFile> images; // Danh sách ảnh (đa tệp)

    private List<Long> taggedUserIds; // Danh sách ID người được gắn thẻ
}
