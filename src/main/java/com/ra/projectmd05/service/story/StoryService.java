package com.ra.projectmd05.service.story;

import com.ra.projectmd05.model.dto.request.StoryRequestDTO;
import com.ra.projectmd05.model.entity.Story;

import java.io.IOException;

public interface StoryService {
    Story addStory(StoryRequestDTO storyRequestDTO) throws IOException;
    void deleteStory(Long storyId);
}
