package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    // Lấy bài viết theo user ID, sắp xếp theo thời gian tạo mới nhất
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

}
