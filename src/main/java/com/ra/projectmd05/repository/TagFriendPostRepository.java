package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.TagFriendPost;
import com.ra.projectmd05.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagFriendPostRepository extends JpaRepository<TagFriendPost, Long> {
    List<TagFriendPost> findAllByPost(Post post);
}
