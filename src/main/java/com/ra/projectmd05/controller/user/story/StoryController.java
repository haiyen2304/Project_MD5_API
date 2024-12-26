package com.ra.projectmd05.controller.user.story;

import com.ra.projectmd05.model.dto.request.StoryRequestDTO;
import com.ra.projectmd05.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<?> addStory(@ModelAttribute StoryRequestDTO storyRequestDTO) throws IOException {
        storyService.addStory(storyRequestDTO);
        return new ResponseEntity<>("Thêm tin thành công.", HttpStatus.CREATED);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addStory2(@ModelAttribute StoryRequestDTO storyRequestDTO) throws IOException {
        storyService.addStory(storyRequestDTO);
        return new ResponseEntity<>("Thêm tin thành công.", HttpStatus.CREATED);
    }

}
