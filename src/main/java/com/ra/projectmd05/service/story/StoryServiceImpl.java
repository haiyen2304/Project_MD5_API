package com.ra.projectmd05.service.story;

import com.ra.projectmd05.model.dto.request.StoryRequestDTO;
import com.ra.projectmd05.model.entity.Story;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.StoryRepository;
import com.ra.projectmd05.service.UploadService;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final UserServiceImpl userServiceImpl;
    private final UploadService uploadService;
    @Override
    public void deleteStory(Long storyId) {
        User user = userServiceImpl.getCurrentUserInfo();
        storyRepository.findByIdAndUserId(storyId, user.getId());
    }

    @Override
    public Story addStory(StoryRequestDTO storyRequestDTO) throws IOException {
        User user = userServiceImpl.getCurrentUserInfo();
        // Kiểm tra loại mediaType hợp lệ
        if (!storyRequestDTO.getMediaType().equals("text") && !storyRequestDTO.getMediaType().equals("image") && !storyRequestDTO.getMediaType().equals("video")) {
            throw new IllegalArgumentException("Invalid mediaType. Allowed values are 'text', 'image', or 'video'.");
        }

        MultipartFile url = storyRequestDTO.getMediaUrl();
        String  imageUrl = uploadService.uploadFile(url); // Lưu file và lấy URL


        // Xây dựng đối tượng Story
        Story story = Story.builder()
                .user(user)
                .mediaType(storyRequestDTO.getMediaType())
                .mediaUrl(storyRequestDTO.getMediaType().equals("text") ? null : imageUrl) // mediaUrl chỉ cần khi là image/video
                .content(storyRequestDTO.getMediaType().equals("text") ? storyRequestDTO.getContent() : null)  // content chỉ cần khi là text
                .createdAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusHours(24))         // Thời gian hết hạn
                .build();

        // Lưu Story vào database
        return storyRepository.save(story);
    }
}
