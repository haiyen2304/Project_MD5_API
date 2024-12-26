package com.ra.projectmd05.model.dto.request;

import com.ra.projectmd05.constants.PostType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostUpdateRequestDTO {
    @Size(min = 1, max = 5000, message = "Nội dung bài đăng phải từ 1 đến 5000 ký tự.")
    private String content;

    private PostType privacy = PostType.PUBLIC;

    private List<MultipartFile> images; // Danh sách ảnh mới
    private List<Long> imageIdsToDelete; // Danh sách ID ảnh cần xóa

    private List<Long> taggedUserIdsToRemove; // Danh sách ID người cần gỡ thẻ
    private List<Long> taggedUserIdsToAdd; // Danh sách ID người cần gắn thẻ mới
}
