package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Story findByIdAndUserId(Long id, Long userId);

}
