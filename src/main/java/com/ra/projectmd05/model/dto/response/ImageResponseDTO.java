package com.ra.projectmd05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageResponseDTO {
    private String url;              // URL ảnh
    private boolean isPrimary;       // Ảnh chính hay không
    private int position;            // Vị trí của ảnh trong danh sách
}
