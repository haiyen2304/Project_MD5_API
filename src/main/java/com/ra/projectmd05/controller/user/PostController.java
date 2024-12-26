package com.ra.projectmd05.controller.user;

import com.ra.projectmd05.model.dto.request.PostRequestDTO;
import com.ra.projectmd05.model.dto.request.PostUpdateRequestDTO;
import com.ra.projectmd05.model.dto.response.PostResponseDTO;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.service.post.PostService;
import com.ra.projectmd05.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/posts")
public class PostController {
    // đăng bài + gắn thẻ +ảnh
    private final PostService postService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<?> createPost(@ModelAttribute PostRequestDTO postRequestDTO) throws IOException {
        postService.createPost(postRequestDTO);
        return new ResponseEntity<>("Bài viết đã được tạo thành công.",HttpStatus.CREATED);
    }

    // sửa , consumes = {"multipart/form-data"}
    @PutMapping(value = "{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @Valid @ModelAttribute PostUpdateRequestDTO postUpdateRequestDTO) throws IOException {
        postService.editPost(postId, postUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Bài viết đã được chỉnh sửa thành công.");
    }

    @GetMapping("/{postID}")
    public ResponseEntity<?> getPost(@PathVariable Long postID) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postID));
    }
//    // hiển thị danh sách bài post (của người đang đăng nhập currentUser)
//    @GetMapping("/postOfUser")
//    public ResponseEntity<?> findPostOfUser(){
//        List<PostResponseDTO> listPostResponseDTO= postService.getPostsByCurrentUser();
//        return new ResponseEntity<>(listPostResponseDTO,HttpStatus.OK);
//    }

    // hiển thị danh sách bài post
    @GetMapping("/listPost/{userId}")
    public ResponseEntity<?> findPost(@PathVariable Long userId) throws IOException {
        List<PostResponseDTO> listPostResponseDTO= postService.getPostsByUser(userId);
        return new ResponseEntity<>(listPostResponseDTO,HttpStatus.OK);
    }
    // lấy ra thông tin người dùng dựa vào ID
    @GetMapping("profile/{userID}")
    public ResponseEntity<?> getProfile(@PathVariable Long userID) throws IOException {
        User user= userService.getUserByIdProfile(userID);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


//    @DeleteMapping("{postId}")
//    public ResponseEntity<String> deletePost(@PathVariable Long postId) throws IOException {
//        postService.deletePost(postId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }



}
