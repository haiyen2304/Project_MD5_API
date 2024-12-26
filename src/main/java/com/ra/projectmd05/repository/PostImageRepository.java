package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPost(Post post);
    int countByPost(Post post);// Đếm số lượng ảnh hiện tại của bài viết
    List<PostImage> findByPost(Post post);// lấy ra danh sách ảnh của bài post
    List<PostImage> findByPostOrderByPosition(Post post);//Tìm danh sách ảnh còn lại của bài viết theo thứ tự:
    boolean existsByPostAndIsPrimary(Post post, boolean isPrimary);//Kiểm tra ảnh chính tồn tại:


    //----------------------HÙNG START----------------------------

    @Modifying
    @Query("DELETE FROM PostImage pr WHERE pr.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
    //----------------------HÙNG END----------------------------

}
