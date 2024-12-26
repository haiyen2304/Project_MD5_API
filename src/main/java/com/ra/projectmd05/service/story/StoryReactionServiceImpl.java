package com.ra.projectmd05.service.story;

import com.ra.projectmd05.model.dto.request.StoryReactionDTO;
import com.ra.projectmd05.model.entity.IconLike;
import com.ra.projectmd05.model.entity.Story;
import com.ra.projectmd05.model.entity.StoryReaction;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.IconLikeRepository;
import com.ra.projectmd05.repository.StoryReactionRepository;
import com.ra.projectmd05.repository.StoryRepository;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class StoryReactionServiceImpl implements StoryReactionService {
    private final UserServiceImpl userServiceImpl;
    private final StoryRepository storyRepository;
    private final IconLikeRepository iconLikeRepository;
    private final StoryReactionRepository storyReactionRepository;
    @Override
    public StoryReaction reactionStory(StoryReactionDTO storyReactionDTO) {
        User user = userServiceImpl.getCurrentUserInfo();
        Story story = storyRepository.findById(storyReactionDTO.getStoryId()).get();
        IconLike iconLike = iconLikeRepository.findById(storyReactionDTO.getIconId()).get();

        if (user != null && story != null && iconLike != null) {
            Optional<StoryReaction> existingReaction = storyReactionRepository.findByStoryIdAndUser_Id(storyReactionDTO.getStoryId(), user.getId());
            if (existingReaction.isPresent()) {
                StoryReaction storyReaction = existingReaction.get();
                storyReaction.setIcon(iconLike);
                return storyReactionRepository.save(storyReaction);
            } else  {
                StoryReaction storyReaction = StoryReaction.builder().
                        user(user).
                        story(story).
                        icon(iconLike).
                        build();

                return storyReactionRepository.save(storyReaction);
            }
        }
        return null;
    }
}
