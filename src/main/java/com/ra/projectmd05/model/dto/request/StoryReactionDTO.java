package com.ra.projectmd05.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoryReactionDTO {
    @NotBlank(message = "hãy nhập storyId")
    private Long storyId;
    @NotBlank(message = "hãy nhập iconId")
    private Long iconId;
}
