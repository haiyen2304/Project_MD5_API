package com.ra.projectmd05.controller.user.post;


import com.ra.projectmd05.model.dto.request.PostReactionRequestDTO;
import com.ra.projectmd05.model.entity.PostLike;
import com.ra.projectmd05.service.post.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/reactionPost")
public class ReactionPostController {

    private final PostReactionService postReactionService;

    @PostMapping
    public ResponseEntity<?> reactionPost(@RequestBody PostReactionRequestDTO postReactionRequestDTO) throws IOException {
      PostLike postReaction = postReactionService.savePostReaction(postReactionRequestDTO);

      if (postReaction != null) {
          return new ResponseEntity<>("Thả cảm xúc thành công.", HttpStatus.CREATED);
      } else  {
          return new ResponseEntity<>("Thả cảm xúc thất bại.", HttpStatus.NO_CONTENT);
      }
    }

}
