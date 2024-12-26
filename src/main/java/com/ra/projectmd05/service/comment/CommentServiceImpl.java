package com.ra.projectmd05.service.comment;

import com.ra.projectmd05.constants.FriendshipStatus;
import com.ra.projectmd05.model.dto.request.CommentRequestDTO;
import com.ra.projectmd05.model.dto.response.CommentDTO;
import com.ra.projectmd05.model.entity.Comment;
import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.model.entity.UserInfo;
import com.ra.projectmd05.repository.CommentRepository;
import com.ra.projectmd05.repository.FriendRepository;
import com.ra.projectmd05.repository.PostRepository;
import com.ra.projectmd05.repository.UserInfoRepository;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserServiceImpl userServiceImpl;
    private final FriendRepository friendRepository;
    private final UserInfoRepository userInfoRepository;
    @Override
    public CommentDTO createComment( CommentRequestDTO commentRequestDT) {
        // 1. Kiểm tra Post tồn tại
        Post post = postRepository.findById(commentRequestDT.getPostId()).orElseThrow(()-> new NoSuchElementException("Post không tồn tại với id: " + commentRequestDT.getPostId()));
        // 2. Kiểm tra User tồn tại
        User user =userServiceImpl.getCurrentUserInfo();
        // 3. Kiểm tra Comment Cha (nếu có)
        // Kiểm tra nếu người dùng bị chặn bởi chủ bài viết
        if (isBlockedBetweenUsers(user.getId(), post.getUser().getId())) {
            throw new IllegalArgumentException("Bạn không thể bình luận trên bài viết này.");
        }
        if (commentRequestDT.getContent() == null || commentRequestDT.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung comment không được để trống!");
        }
        Comment parentComment = null;
        if(commentRequestDT.getParentId() != null){
            parentComment = commentRepository.findById(commentRequestDT.getParentId()).orElseThrow(()-> new NoSuchElementException("Comment cha không tồn tại với id: " + commentRequestDT.getParentId()));
        }
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentRequestDT.getContent())
                .parent(parentComment)
                .createdAt(LocalDate.now())
                .status(true)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    @Override
    public List<CommentDTO> getParentComments(Long postId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();

        // 1. Kiểm tra comment cha
        postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post không tồn tại với id: " + postId));

        // 2. Lấy tất cả comment cha kèm số lượng comment con
        List<Object[]> results = commentRepository.findParentCommentsWithChildCount(postId);

        // 3. Chuyển đổi và lọc dữ liệu
        return results.stream()
                .map(this::convertToParentCommentDTO)
                .filter(commentDTO -> !isBlockedBetweenUsers(currentUser.getId(), commentDTO.getUserId())) // ktra chặn
                .filter(commentDTO -> isVisibleComment(currentUser, commentDTO))// kiểm tra ăn
                .toList();
    }
    private boolean isVisibleComment(User currentUser, CommentDTO commentDTO) {
        // Bình luận bị ẩn nhưng chủ bài viết và chủ comment vẫn xem được
        return commentDTO.getStatus() ||
                commentDTO.getUserId().equals(currentUser.getId()) ||
                isPostOwner(currentUser, commentDTO.getPostId());
    }

    private boolean isPostOwner(User currentUser, Long postId) {
        return postRepository.findById(postId)
                .map(post -> post.getUser().getId().equals(currentUser.getId()))
                .orElse(false);
    }

    @Override
    public List<CommentDTO> getChildComments(Long parentId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();

        // 1. Kiểm tra comment cha
        commentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Comment cha không tồn tại với id: " + parentId));

        // 2. Lấy tất cả comment con và lọc
        List<Comment> childComments = commentRepository.findAllByParentId(parentId);

        return childComments.stream()
                .map(this::convertToDTO)
                .filter(commentDTO -> !isBlockedBetweenUsers(currentUser.getId(), commentDTO.getUserId()))  // kiểm tra chặn
                .filter(commentDTO -> isVisibleComment(currentUser, commentDTO))// kiểm tra ẩn
                .toList();
    }


    @Override
    public CommentDTO updateComment(Long commentId, String newContent) {
        User user = userServiceImpl.getCurrentUserInfo();

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment không tồn tại với id: " + commentId));
        // Kiểm tra quyền sở hữu comment
        if (!existingComment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền sửa comment này.");
        }
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung comment không được để trống!");
        }
        // 3. Cập nhật nội dung comment
        existingComment.setContent(newContent);
        Comment updatedComment = commentRepository.save(existingComment);
        return convertToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        // 1. Tìm comment cần xóa
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment không tồn tại với id: " + commentId));

        boolean isPostOwner = existingComment.getPost().getUser().getId().equals(currentUser.getId());
        boolean isCommentOwner = existingComment.getUser().getId().equals(currentUser.getId());

        // 2. Kiểm tra quyền xóa: chỉ chủ bài viết hoặc chủ comment mới được xóa
        if (!isPostOwner && !isCommentOwner) {
            throw new IllegalArgumentException("Bạn không có quyền xóa comment này.");
        }
        commentRepository.deleteAllByParentId(commentId);
        commentRepository.delete(existingComment);

    }

    @Override
    public CommentDTO hideComment(Long commentId, Long userId) {
        // 1. Lấy comment theo ID
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment không tồn tại với id: " + commentId));
        // 2. Lấy bài viết liên quan đến comment
        Post post = existingComment.getPost();
        // 3. Kiểm tra quyền ẩn comment
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Bạn không có quyền ẩn comment này vì bạn không phải chủ sở hữu bài đăng.");
        }
        // 4. Kiểm tra xem bình luận có phải của chủ sở hữu bài đăng hay không
        if (existingComment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Bạn không thể ẩn bình luận của chính mình trên bài đăng.");
        }
        // 5. Kiểm tra trạng thái comment
        if (!existingComment.getStatus()) { // Nếu đã ẩn (status == false)
            throw new IllegalStateException("Comment đã bị ẩn trước đó, không thể ẩn lại.");
        }
        // 6. Ẩn comment cha
        existingComment.setStatus(false); // false -> comment bị ẩn
        commentRepository.save(existingComment);
        // 7. Tìm và ẩn tất cả comment con
        List<Comment> childComments = commentRepository.findByParentId(commentId);
        for (Comment childComment : childComments) {
            childComment.setStatus(false);
        }
        commentRepository.saveAll(childComments);
        return convertToDTO(existingComment);
    }

    // nếu có nhiều cấp
    private void hideChildComments(Long parentId) {
        List<Comment> childComments = commentRepository.findByParentId(parentId);
        for (Comment child : childComments) {
            child.setStatus(false);
            commentRepository.save(child); // Lưu từng comment
            hideChildComments(child.getId()); // Đệ quy để ẩn comment con
        }
    }



    private CommentDTO convertToDTO(Comment comment) {
        UserInfo userInfo = userInfoRepository.findByUserId(comment.getUser().getId()).get();
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .status(comment.getStatus())
                .childCommentCount(commentRepository.countByParentId(comment.getId())) // Truy vấn đếm số comment con
                .avatarUrlUser(userInfo.getAvatar())
                .userName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                .build();
    }

    private CommentDTO convertToParentCommentDTO(Object[] result) {
        Comment comment = (Comment) result[0];
        Long childCount = (Long) result[1];
        UserInfo userInfo = userInfoRepository.findByUserId(comment.getUser().getId()).get();

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(null) // Comment cha không có parentId
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .status(comment.getStatus())
                .childCommentCount(childCount) // Số lượng comment con
                .avatarUrlUser(userInfo.getAvatar())
                .userName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                .build();
    }

//kiểm tra xem hai người dùng có chặn nhau hay không
    private boolean isBlockedBetweenUsers(Long userId, Long otherUserId) {
        return friendRepository.existsByUserIdAndFriendUserIdAndStatus(userId, otherUserId, FriendshipStatus.BLOCKED)
                || friendRepository.existsByUserIdAndFriendUserIdAndStatus(otherUserId, userId, FriendshipStatus.BLOCKED);
    }

}
