package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.StoryReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryReactionRepository extends JpaRepository<StoryReaction, Long> {

    Optional<StoryReaction> findByStoryIdAndUser_Id(Long storyId, Long userId);
}
