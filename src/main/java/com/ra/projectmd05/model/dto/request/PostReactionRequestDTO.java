package com.ra.projectmd05.model.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostReactionRequestDTO {
    @NotBlank(message = "hãy nhập postId")
    private Long postId;
    @NotBlank(message = "hãy nhập iconId")
    private Long iconId;
}
