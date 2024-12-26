package com.ra.projectmd05.repository;
import com.ra.projectmd05.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostLike,Long> {

    //----------------------HÙNG START----------------------------
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
    @Modifying
    @Query("DELETE FROM PostLike pr WHERE pr.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
    //----------------------HÙNG END----------------------------

}
