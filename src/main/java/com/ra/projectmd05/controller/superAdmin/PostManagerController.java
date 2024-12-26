package com.ra.projectmd05.controller.superAdmin;

import com.ra.projectmd05.model.dto.response.PostsResponseDTO;
import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/superAdmin/posts")
@RequiredArgsConstructor
public class PostManagerController {

    private final PostService postService;

    // Hiển thị danh sách bài đăng
    @GetMapping
    public ResponseEntity<Page<PostsResponseDTO>> listPosts(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
//        Page<PostsResponseDTO> posts = postService.getAllPostsAsDTO(pageable);
//        return ResponseEntity.ok(posts);
        return null;
    }

    // Tìm kiếm bài đăng theo từ khóa
    @GetMapping("/search")
    public ResponseEntity<List<PostsResponseDTO>> searchPosts(@RequestParam String keyword) {
//        List<PostsResponseDTO> postsResponse = postService.searchPostsByKeyword(keyword);
//        return ResponseEntity.ok(postsResponse);
        return null;
    }

    // Ẩn bài viết (khi bị báo cáo nhưng chưa quyết định)
    @PutMapping("/{postId}/toggle-hide")
    public ResponseEntity<PostsResponseDTO> toggleHidePost(@PathVariable Long postId) {
//        PostsResponseDTO updatedPost = postService.toggleHidePost(postId);

//        return ResponseEntity.ok(updatedPost);
        return null;
    }
}
