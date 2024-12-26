package com.ra.projectmd05.service.post;

import com.ra.projectmd05.model.dto.request.PostRequestDTO;
import com.ra.projectmd05.model.dto.request.PostUpdateRequestDTO;
import com.ra.projectmd05.model.dto.response.PostResponseDTO;
import com.ra.projectmd05.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post createPost(PostRequestDTO postRequestDTO) throws IOException;
    Post convertPostDTOToPost(PostRequestDTO postRequestDTO);
    // chỉnh  sửa bài viết=> thì cần xem được bài viét, xem được bài viết thì  cân check quyền
    //Bạn không có quyền xem bài viết này.
    boolean canViewPost( Post post);
    // lấy bài viết dựa vào ID
    Post getPostById(Long postTd);
    // chỉnh sửa bài viết
    Post editPost(Long postId, PostUpdateRequestDTO postUpdateRequestDTO) throws IOException;
    // hiển thị danh sách bài post (của người đang đăng nhập currentUser)
    List<PostResponseDTO> getPostsByUser(Long userId);
    // hiển thị danh sách bài post ở trang cá nhân 1 người ở chế độ công khai và chế độ bàn bè) - nếu 2 người là bạn
    // chỉ hiển thị danh sách bài post  ở chế độ công khai nếu chưa là bạn bè

    // hiển thị danh sách bài post (của người đang đăng nhập và của bạn bè, và của các group đang tham gia);

}
