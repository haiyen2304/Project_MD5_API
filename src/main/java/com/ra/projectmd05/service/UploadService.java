package com.ra.projectmd05.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadService {
    private final Cloudinary cloudinary;

    @Autowired
    public UploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Lấy tên file gốc từ MultipartFile (xóa đuôi file nếu cần xử lý thêm)
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }

        // Thêm tên file vào tham số upload (public_id)
        Map uploadParams = ObjectUtils.asMap(
                "public_id", originalFilename // Đặt tên file hiển thị đúng tên gốc
        );

        // Upload file lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

        // Trả về URL của file đã upload
        return uploadResult.get("url").toString();
    }

}
