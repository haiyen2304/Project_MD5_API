package com.ra.projectmd05.service.story;

import com.ra.projectmd05.model.dto.request.StoryReactionDTO;
import com.ra.projectmd05.model.entity.StoryReaction;

public interface StoryReactionService {

    StoryReaction reactionStory(StoryReactionDTO storyReactionDTO);
}
