package com.ra.projectmd05.service.post;

import com.ra.projectmd05.model.dto.request.PostReactionRequestDTO;
import com.ra.projectmd05.model.entity.IconLike;
import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.PostLike;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.PostReactionRepository;
import com.ra.projectmd05.service.iconLike.IconLikeServiceImpl;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {
    private final UserServiceImpl userServiceImpl;
    private final PostServiceImpl postServiceImpl;
    private final IconLikeServiceImpl iconLikeServiceImpl;
    private final PostReactionRepository postReactionRepository;

    @Override
    public PostLike savePostReaction(PostReactionRequestDTO postReactionRequestDTO) {
        User user = userServiceImpl.getCurrentUserInfo();
        Post post = postServiceImpl.getPostById(postReactionRequestDTO.getPostId());
        IconLike iconLike = iconLikeServiceImpl.getIconLikeById(postReactionRequestDTO.getIconId());

        if (iconLike != null && post != null) {
            // Kiểm tra xem phản ứng đã tồn tại hay chưa
            Optional<PostLike> existingReaction = postReactionRepository.findByPostIdAndUserId(post.getId(), user.getId());

            if (existingReaction.isPresent()) {
                // Cập nhật icon nếu phản ứng đã tồn tại
                PostLike postReaction = existingReaction.get();
                postReaction.setIcon(iconLike);
                return postReactionRepository.save(postReaction);
            } else {
                // Tạo phản ứng mới nếu chưa tồn tại
                PostLike postReaction = PostLike.builder()
                        .user(user)
                        .post(post)
                        .icon(iconLike)
                        .build();
                return postReactionRepository.save(postReaction);
            }
        }

        return null;
    }
}
