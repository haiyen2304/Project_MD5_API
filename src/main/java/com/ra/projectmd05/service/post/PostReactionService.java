package com.ra.projectmd05.service.post;

import com.ra.projectmd05.model.dto.request.PostReactionRequestDTO;
import com.ra.projectmd05.model.entity.PostLike;

public interface PostReactionService {

    PostLike savePostReaction(PostReactionRequestDTO postReactionRequestDTO);
}
