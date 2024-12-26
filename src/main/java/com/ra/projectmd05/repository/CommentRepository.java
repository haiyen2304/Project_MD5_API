package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //-------------------- hai yen--------------------
    // Lấy tất cả comment con theo parentId
    List<Comment> findAllByParentId(Long parentId);

    // Lấy tất cả comment theo postId
    List<Comment> findAllByPostId(Long postId);

    // Lấy tất cả comment cha (parentId == null)
    List<Comment> findAllByParentIsNullAndPostId(Long postId);

    Long countByParentId(Long parentId);

    @Query("SELECT c, COUNT(child) FROM Comment c LEFT JOIN c.parent child " +
            "WHERE c.parent IS NULL AND c.post.id = :postId " +
            "GROUP BY c")
    List<Object[]> findParentCommentsWithChildCount(Long postId);
    void deleteAllByParentId(Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId")
    List<Comment> findByParentId(Long parentId);
    //-------------------- hai yen End--------------------
}
