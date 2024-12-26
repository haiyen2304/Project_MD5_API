package com.ra.projectmd05.controller.user.post;


import com.ra.projectmd05.service.post.hiddenPost.HiddenPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/hiddenPost")
public class HiddenPost {

    private final HiddenPostService hiddenPostService;

    @PostMapping("{postId}")
    public ResponseEntity<?> reactionPost(@PathVariable Long postId) throws IOException {

         hiddenPostService.hidePostForUser(postId);
         return new ResponseEntity<>(HttpStatus.OK);
    }
}
