package com.ra.projectmd05.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaggedUserResponseDTO {
    private Long id;                 // ID người dùng
    private String name;             // Tên người dùng
}
