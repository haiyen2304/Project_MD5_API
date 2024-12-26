package com.ra.projectmd05.controller.user.story;


import com.ra.projectmd05.model.dto.request.StoryReactionDTO;
import com.ra.projectmd05.model.entity.StoryReaction;
import com.ra.projectmd05.service.story.StoryReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/storyReaction")
public class StoryReactionController {

    private final StoryReactionService storyReactionService;

    @PostMapping
    public ResponseEntity<?> storyReaction(@RequestBody StoryReactionDTO storyReactionDTO) {
        StoryReaction storyReaction = storyReactionService.reactionStory(storyReactionDTO);
        if (storyReaction != null) {
            return new ResponseEntity<>("bày tỏ cảm xúc thành công.", HttpStatus.CREATED);
        }else  {
            return new ResponseEntity<>("bày tỏ cảm xúc thất bại.", HttpStatus.NO_CONTENT);
        }

    }
}
