package com.ra.projectmd05.service.comment;

import com.ra.projectmd05.model.dto.request.CommentRequestDTO;
import com.ra.projectmd05.model.dto.response.CommentDTO;
import com.ra.projectmd05.model.entity.Comment;

import java.util.List;

public interface CommentService {
    //----------------------Haiyen ----------------------
    //Tạo mới Comment (những người đã chặn nhau không comment được vào bài của nhau)
    CommentDTO createComment( CommentRequestDTO commentRequestDT);
    // lấy các comment cha:
    List<CommentDTO> getParentComments(Long postId);
    //lấy comment con:
    List<CommentDTO> getChildComments(Long parentId);
    // sửa
    CommentDTO updateComment(Long commentId, String newContent);
    // xóa
    void deleteComment(Long commentId);
    // chủ sở hữu của bài đăng có quyền ẩn comment các comment trong bài đăng đó
    CommentDTO hideComment(Long commentId, Long userId);

    //----------------------Haiyen End----------------------
}
