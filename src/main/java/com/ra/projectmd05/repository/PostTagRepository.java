package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.TagFriendPost;
import com.ra.projectmd05.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<TagFriendPost, Long> {
    void deleteByPost(Post post);
    void deleteByPostAndTaggedUserId(Post post, Long taggedUserId);//Xóa tag của người dùng cụ thể từ bài viết:
    boolean existsByPostAndTaggedUser(Post post, User taggedUser);//Kiểm tra nếu một người đã được gắn thẻ:
}
