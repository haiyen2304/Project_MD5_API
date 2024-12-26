package com.ra.projectmd05.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequestDTO {
    @NotBlank(message = "vui lòng nhập postId")
    private Long postId;
    @NotBlank(message = "vui lòng nhập lý do")
    private String reason;
}
