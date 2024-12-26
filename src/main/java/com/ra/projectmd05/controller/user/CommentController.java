package com.ra.projectmd05.controller.user;

import com.ra.projectmd05.model.dto.request.CommentRequestDTO;
import com.ra.projectmd05.model.dto.response.CommentDTO;
import com.ra.projectmd05.model.entity.Comment;
import com.ra.projectmd05.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/comments")
public class CommentController {
    private final CommentService commentService;

    // 1. Tạo comment mới
    @PostMapping("/createComment")
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDTO commentRequestDTO){
        CommentDTO newComment = commentService.createComment(commentRequestDTO);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
    // 2. Hiển thị tất cả comment cha của một bài viết
    @GetMapping("/parents/{postId}")
    public ResponseEntity<List<CommentDTO>> getParentComments(@PathVariable Long postId) {
        List<CommentDTO> parentComments = commentService.getParentComments(postId);
        return new ResponseEntity<>(parentComments, HttpStatus.OK);
    }



    //3. Hiển thị tất cả comment con của một comment cha
    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<CommentDTO>> getChildComments(@PathVariable Long parentId) {
        List<CommentDTO> childComments = commentService.getChildComments(parentId);
        return new ResponseEntity<>(childComments, HttpStatus.OK);
    }

    //4. Cập nhật nội dung comment
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestParam String newContent) {
        System.out.println(newContent);

        CommentDTO updatedComment = commentService.updateComment(commentId, newContent);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
    //5. Xóa comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //6. ẩn nội dung comment
    @PutMapping("/hide/{commentId}/{userId}")
    public ResponseEntity<CommentDTO> hideComment(
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        CommentDTO updatedComment = commentService.hideComment(commentId, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }






}
