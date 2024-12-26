package com.ra.projectmd05.service.post;

import com.ra.projectmd05.constants.FriendshipStatus;
import com.ra.projectmd05.constants.PostType;
import com.ra.projectmd05.model.dto.request.PostRequestDTO;
import com.ra.projectmd05.model.dto.request.PostUpdateRequestDTO;
import com.ra.projectmd05.model.dto.response.ImageResponseDTO;
import com.ra.projectmd05.model.dto.response.PostResponseDTO;
import com.ra.projectmd05.model.dto.response.TaggedUserResponseDTO;
import com.ra.projectmd05.model.entity.*;
import com.ra.projectmd05.repository.*;
import com.ra.projectmd05.service.UploadService;
import com.ra.projectmd05.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;
    private final UserServiceImpl userServiceImpl;
    private final PostImageRepository postImageRepository;
    private final UploadService uploadService;
    private final FriendRepository friendRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PostImageRepository postimageRepository;
    private final TagFriendPostRepository tagFriendPostRepository;

    @Override
    public Post createPost(PostRequestDTO postRequestDTO) throws IOException {
        validateImages(postRequestDTO.getImages());
        Post post = convertPostDTOToPost(postRequestDTO);
        post = postRepository.save(post);
        //Xử lý danh sách ảnh
        if(postRequestDTO.getImages() != null && !postRequestDTO.getImages().isEmpty()){
            List<MultipartFile> images = postRequestDTO.getImages();
            for(int i = 0; i < images.size(); i++){
                MultipartFile image = images.get(i);
                // Upload ảnh lên Cloudinary
                String  imageUrl = uploadService.uploadFile(image); // Lưu file và lấy URL
                // Tạo PostImage và lưu vào DB
                PostImage postImage = PostImage.builder()
                        .post(post)
                        .url(imageUrl)
                        .createdAt(LocalDateTime.now())
                        .isPrimary(i==0) // Ảnh đầu tiên là ảnh chính
                        .position(i + 1) // Thứ tự của ảnh
                        .build();
                postImageRepository.save(postImage);
            }
        }

        // gắn the ban be
        if(postRequestDTO.getTaggedUserIds() != null && !postRequestDTO.getTaggedUserIds().isEmpty()){
            List<Long> taggedUserIds = postRequestDTO.getTaggedUserIds();// Lấy danh sách ID người dùng được gắn thẻ từ DTO
            User currentUser = userServiceImpl.getCurrentUserInfo();     // Lấy thông tin người dùng hiện tại
            // Lọc ra danh sách bạn bè có trạng thái "ACCEPTED"
            List<User> acceptedFriends = findAcceptedFriends(currentUser.getId(), taggedUserIds);
            for (User taggedUser : acceptedFriends) {// Tạo PostTag cho mỗi bạn bè được gắn thẻ
                TagFriendPost postTag = TagFriendPost.builder()
                        .post(post)
                        .taggedUser(taggedUser)
                        .taggedBy(currentUser)
                        .createdAt(LocalDateTime.now())
                        .build();
                postTagRepository.save(postTag); // Lưu thông tin gắn thẻ vào DB
            }
        }
        return postRepository.save(post);
    }


    @Override
    public Post convertPostDTOToPost(PostRequestDTO postRequestDTO) {
        User user =userServiceImpl.getCurrentUserInfo();
        return Post.builder()
                .user(user)
                .content(postRequestDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .postType(postRequestDTO.getPrivacy())
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .hasImage(postRequestDTO.getImages() != null && !postRequestDTO.getImages().isEmpty())
                .build();
    }

    @Override
    public boolean canViewPost(Post post) {
        if (post.getPostType() == PostType.PUBLIC) {
            return true;
        }
        User currentUser=  userServiceImpl.getCurrentUserInfo();
        // Kiểm tra nếu bài viết là "Chỉ mình tôi"
        if (post.getPostType() == PostType.ONLY_ME) {
            return currentUser.getId().equals(post.getUser().getId());
        }
        // Kiểm tra nếu bài viết là "Chỉ bạn bè"
        if(post.getPostType() == PostType.FRIENDS ){
            return friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUser.getId(),post.getUser().getId(), FriendshipStatus.ACCEPTED);

        }
        return false;
    }


//    @Transactional
//    @Override
//    public void deletePost(Long postTd) {
//        postImageRepository.deleteByPostId(postTd);
//        postReactionRepository.deleteByPostId(postTd);
//        postRepository.deleteById(postTd);
//    }

    @Override
    public Post getPostById(Long postTd) {
        Post post =postRepository.findById(postTd).orElseThrow(()-> new NoSuchElementException("Bài viết không tồn tại"));
        if(!canViewPost(post)) {
            throw new AccessDeniedException("Bạn không có quyền xem bài viết này");
        }
        return post;
    }

    @Override
    public Post editPost(Long postId, PostUpdateRequestDTO postUpdateRequestDTO) throws IOException {
        //lấy ra bài post dựa ID
        Post post = getPostById(postId);
        // check quyền xem
        canViewPost(post);
        // check quyên chỉnh sửa
        User currentUser = userServiceImpl.getCurrentUserInfo();
        if(!currentUser.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Bạn không có quyền sửa bài viết này");
        }
        // sửa
        //-----------------time-------------
        post.setUpdatedAt(LocalDateTime.now());
        if (postUpdateRequestDTO.getContent() != null) {//content
            post.setContent(postUpdateRequestDTO.getContent());
        }
        //-----------------quyền riêng tư hay không----------------
        if (postUpdateRequestDTO.getPrivacy() != null) {// quyền
            post.setPostType(postUpdateRequestDTO.getPrivacy());
        }
        //----ảnh---
        //------------- xóa ảnh cũ 1 hoặc nhiều-------------
        if(postUpdateRequestDTO.getImageIdsToDelete() != null && !postUpdateRequestDTO.getImageIdsToDelete().isEmpty()) {
            boolean isPrimaryDeleted = false; // Cờ kiểm tra xem ảnh chính có bị xóa không
            for(Long imageId : postUpdateRequestDTO.getImageIdsToDelete()) { // duyệt danh sách ảnh muốn xóa
                PostImage imageToDelete  = postImageRepository.findById(imageId).orElseThrow(() -> new NoSuchElementException("Ảnh không tồn tại"));
                if (imageToDelete.getIsPrimary()) {
                    isPrimaryDeleted = true; // Đánh dấu rằng ảnh chính đã bị xóa
                }
                postImageRepository.delete(imageToDelete);
            }
            // Nếu ảnh chính bị xóa và không có ảnh mới, chọn ảnh đầu tiên trong danh sách hiện tại làm ảnh chính
            if (isPrimaryDeleted && (postUpdateRequestDTO.getImages() == null || postUpdateRequestDTO.getImages().isEmpty())) {
                List<PostImage> remainingImages = postImageRepository.findByPostOrderByPosition(post); //danh sách ảnh còn lại của bài viết theo thứ tự:

                if (!remainingImages.isEmpty()) {
                    PostImage newPrimaryImage = remainingImages.get(0); // Chọn ảnh đầu tiên làm ảnh chính
                    newPrimaryImage.setIsPrimary(true);
                    postImageRepository.save(newPrimaryImage);
                }
            }
        }
        //-------------Thêm ảnh mới nếu được cung cấp-------------
        if (postUpdateRequestDTO.getImages() != null && !postUpdateRequestDTO.getImages().isEmpty()) {
            List<MultipartFile> newImages = postUpdateRequestDTO.getImages();

            // Kiểm tra xem bài viết hiện tại đã có ảnh chính chưa
            boolean hasPrimaryImage = postImageRepository.existsByPostAndIsPrimary(post, true);

            for (int i = 0; i < newImages.size(); i++) {
                MultipartFile image = newImages.get(i);
                String imageUrl = uploadService.uploadFile(image); // Upload và lấy URL

                PostImage postImage = PostImage.builder()
                        .post(post)
                        .url(imageUrl)
                        .isPrimary(!hasPrimaryImage && i == 0) // Ảnh đầu tiên thêm mới làm ảnh chính nếu không có ảnh chính
                        .position(postImageRepository.countByPost(post) + i + 1)
                        .createdAt(LocalDateTime.now())
                        .build();
                postImageRepository.save(postImage);

                hasPrimaryImage = true; // Sau khi thêm ảnh chính, các ảnh tiếp theo sẽ không phải ảnh chính
            }
        }
        //-----------------tag bạn bè----------------
        if (postUpdateRequestDTO.getTaggedUserIdsToRemove() != null) {
            List<Long> taggedUserIdsToRemove = postUpdateRequestDTO.getTaggedUserIdsToRemove();

            // Xóa các tag chỉ định
            for (Long userIdToRemove : taggedUserIdsToRemove) {
                postTagRepository.deleteByPostAndTaggedUserId(post, userIdToRemove);
            }
        }if (postUpdateRequestDTO.getTaggedUserIdsToAdd() != null) {
            List<Long> taggedUserIdsToAdd = postUpdateRequestDTO.getTaggedUserIdsToAdd();

            // Lọc danh sách bạn bè đã chấp nhận (trạng thái ACCEPTED)
            if (!taggedUserIdsToAdd.isEmpty()) {

                // Lọc tất cả bạn bè có trạng thái ACCEPTED trong danh sách taggedUserIdsToAdd
                List<User> acceptedFriends = findAcceptedFriends(currentUser.getId(), taggedUserIdsToAdd);
                if(acceptedFriends.isEmpty()){
                    throw new BadRequestException("không phải là bạn");
                }
                // Thêm tag mới
                for (User taggedUser : acceptedFriends) {
                    // Kiểm tra xem người dùng đã được gắn thẻ chưa
                    boolean alreadyTagged = postTagRepository.existsByPostAndTaggedUser(post, taggedUser);

                    // Nếu chưa được gắn thẻ
                    if (!alreadyTagged) {
                        // Tạo và lưu tag mới
                        TagFriendPost postTag = TagFriendPost.builder()
                                .post(post)
                                .taggedBy(currentUser) // Người dùng hiện tại là người gắn thẻ
                                .taggedUser(taggedUser) // Người được gắn thẻ
                                .createdAt(LocalDateTime.now())
                                .build();
                        postTagRepository.save(postTag);
                    }
                }
            }
        }
       return postRepository.save(post);
    }

    public List<User> findAcceptedFriends(Long currentUserId, List<Long> taggedUserIds) {
        return userRepository.findAllById(taggedUserIds).stream()
                .filter(user -> friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUserId, user.getId(), FriendshipStatus.ACCEPTED))
                .collect(Collectors.toList());
    }

    private void validateImages(List<MultipartFile> images) {
        if (images != null && !images.isEmpty()) {
            // Kiểm tra số lượng ảnh
            if (images.size() > 10) {
                throw new IllegalArgumentException("Số lượng ảnh tối đa là 10.");
            }
            for (MultipartFile image : images) {
                // Kiểm tra kích thước file (giới hạn 5MB)
                if (image.getSize() > 5 * 1024 * 1024) {
                    throw new IllegalArgumentException("Kích thước file không được vượt quá 5MB: " + image.getOriginalFilename());
                }
                // Kiểm tra định dạng file
                String contentType = image.getContentType();
                if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    throw new IllegalArgumentException("Chỉ cho phép định dạng JPG hoặc PNG: " + image.getOriginalFilename());
                }
            }
        }
    }

    @Override
    public List<PostResponseDTO> getPostsByUser(Long userId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        List<Post> posts =postRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // Kiểm tra quan hệ giữa currentUserLoginNow và user
        boolean isOwner = userId.equals(currentUser.getId());
        boolean isFriend = friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUser.getId(), userId, FriendshipStatus.ACCEPTED);
        // Lọc danh sách bài viết theo quyền riêng tư
        List<Post> filteredPosts = posts.stream().filter(post -> {
            // Nếu là trang cá nhân của chính mình, xem tất cả bài viết
            if (isOwner) {
                return true;
            }
            // Nếu là bạn bè, chỉ xem bài viết PUBLIC hoặc FRIENDS
            if (isFriend) {
                return  post.getPostType().equals(PostType.PUBLIC) || post.getPostType().equals(PostType.FRIENDS);
            }
            // Nếu không phải bạn bè, chỉ xem bài viết PUBLIC
            return post.getPostType().equals(PostType.PUBLIC);
        }).toList();

        // Chuyển đổi danh sách bài viết từ Entity sang DTO
        return filteredPosts.stream()
                .map(this::convertToPostResponseDTO) // Sử dụng phương thức chuyển đổi
                .collect(Collectors.toList());
    }


    //Chuyển đổi một bài viết từ Entity Post sang DTO PostResponseDTO.
    private PostResponseDTO convertToPostResponseDTO(Post post){
        UserInfo userInfo = userInfoRepository.findByUserId(post.getUser().getId()).orElseThrow(()->new NoSuchElementException("User không tồn tại"));
        List<PostImage> postImageList =postimageRepository.findByPost(post);
        List<TagFriendPost> tagFriendPostList=tagFriendPostRepository.findAllByPost(post);
        return PostResponseDTO.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .privacy(post.getPostType())
                .userId(post.getUser().getId())
                .userName(post.getUser().getFirstName() + " " + post.getUser().getLastName())
                .avatarUrl(userInfo.getAvatar())  // lấy ra userInfo
                .images(convertImagesToDTO(postImageList))
                .taggedUsers(convertTaggedUsersToDTO(tagFriendPostList))
                .build();
    }
    // Chuyển đổi danh sách ảnh từ Entity sang DTO-image.
    private List<ImageResponseDTO> convertImagesToDTO(List<PostImage> images){
        return  images.stream().map(image->ImageResponseDTO.builder()
                .url(image.getUrl())
                .isPrimary(image.getIsPrimary())
                .position(image.getPosition()).build()).collect(Collectors.toList());
    }

    //Chuyển đổi danh sách người được gắn thẻ từ Entity sang DTO-use.
    private List<TaggedUserResponseDTO> convertTaggedUsersToDTO (List<TagFriendPost> taggedUsers){
        return taggedUsers.stream()
                .map(taggedUser -> TaggedUserResponseDTO.builder()
                        .id(taggedUser.getId())
                        .name(taggedUser.getTaggedUser().getFirstName() + " " +taggedUser.getTaggedUser().getLastName())
                        .build()).collect(Collectors.toList());
    }

}

