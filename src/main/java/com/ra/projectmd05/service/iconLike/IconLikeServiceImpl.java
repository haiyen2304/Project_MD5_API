package com.ra.projectmd05.service.iconLike;

import com.ra.projectmd05.model.entity.IconLike;
import com.ra.projectmd05.repository.IconLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IconLikeServiceImpl implements IconLikeService {

    private final IconLikeRepository iconLikeRepository;

    @Override
    public IconLike getIconLikeById(long id) {
        IconLike iconLike = iconLikeRepository.findById(id).orElse(null);
        return iconLike;
    }
}
