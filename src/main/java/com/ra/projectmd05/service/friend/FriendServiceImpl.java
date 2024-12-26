package com.ra.projectmd05.service.friend;

import com.ra.projectmd05.constants.FriendshipStatus;
import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.model.dto.response.SuggestedFriendDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.Friend;
import com.ra.projectmd05.model.entity.GroupDetail;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.model.entity.UserInfo;
import com.ra.projectmd05.model.entity.baseObject.BaseObject;
import com.ra.projectmd05.repository.FriendRepository;
import com.ra.projectmd05.repository.GroupDetailRepository;
import com.ra.projectmd05.repository.UserInfoRepository;
import com.ra.projectmd05.repository.UserRepository;
import com.ra.projectmd05.service.auth.AuthServiceImpl;
import com.ra.projectmd05.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final GroupDetailRepository groupDetailRepository;
    private final AuthServiceImpl authServiceImpl;
    private final UserInfoRepository userInfoRepository;
    @Override
    public Friend sendFriendRequest(Long friendUserId) {

        // người gửi  lời mời
        User currentUser = userServiceImpl.getCurrentUserInfo();
        if (currentUser.getId().equals(friendUserId)) {
            throw new IllegalArgumentException("Bạn không thể gửi lời mời kết bạn cho chính mình.");
        }

        // Kiểm tra xem lời mời đã tồn tại hay chưa //cặp bạn đã tồn tại hay chưa
        if (friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.ACCEPTED)) {
            throw new RuntimeException("Đã là bạn bè.");
        }
        if (friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING)) {
            throw new RuntimeException("Đã gửi lời mời kết bạn.");
        }
        // ngườinhajaan lời mời
        User friendUser = userRepository.findById(friendUserId).orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại."));// Lấy thông tin người dùng được gửi lời mời
        // Tạo đối tượng Friend
        Friend friend = Friend.builder()
                .user(currentUser)
                .friendUser(friendUser)
                .createdAt(LocalDateTime.now())
                .status(FriendshipStatus.PENDING)
                .build();
        return friendRepository.save(friend);
    }
    // hiển thị danh sách bạn bè
    @Override
    public List<UserRegisterResponseDTO> findFriends() {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        List<User> friends = friendRepository.findAllByUserIdAndStatus(currentUser.getId(),FriendshipStatus.ACCEPTED);
        return friends.stream()
                .map(authServiceImpl::userConvertToResponseDTO) // Sử dụng hàm chuyển đổi
                .collect(Collectors.toList());
    }
    // Chấp nhận lời mời kết bạn:
    @Override
    public void acceptFriendRequest(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        Friend friendRequest = friendRepository.findByFriendUserIdAndUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING);
        // Kiểm tra nếu yêu cầu kết bạn tồn tại
        if (friendRequest == null) {
            // Nếu không tìm thấy yêu cầu PENDING, tìm yêu cầu khác (PENDING từ phía người kia)
            friendRequest = friendRepository.findByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING)
                    .orElseThrow(() -> new NoSuchElementException("Lời mời kết bạn không tồn tại"));
        }
        friendRequest.setStatus(FriendshipStatus.ACCEPTED);
        friendRequest.setUpdatedAt(LocalDateTime.now());

        friendRepository.save(friendRequest);

    }

    // Hủy lời mời kết bạn (Lời mời chưa được chấp nhận)
    @Override
    public void cancelFriendRequest(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        // Kiểm tra yêu cầu kết bạn từ người gửi đến người nhận
        Friend friendRequest = friendRepository.findByFriendUserIdAndUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING);

        if (friendRequest == null) {
            // Nếu không tìm thấy, kiểm tra chiều ngược lại: từ người nhận đến người gửi
            friendRequest = friendRepository.findByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING)
                    .orElseThrow(() -> new NoSuchElementException("Lời mời kết bạn không tồn tại"));
        }

        // Xóa lời mời kết bạn
        friendRepository.delete(friendRequest);
    }
    // Xóa  bạn:
    @Override
    public void removeFriend(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        // Kiểm tra quan hệ bạn bè giữa người gửi và người nhận
        Friend friend = friendRepository.findByFriendUserIdAndUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.ACCEPTED);

        if (friend == null) {
            // Nếu không tìm thấy, kiểm tra chiều ngược lại: từ người nhận đến người gửi
            friend = friendRepository.findByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new NoSuchElementException("Không phải bạn bè"));
        }

        // Xóa mối quan hệ bạn bè
        friendRepository.delete(friend);
    }
    // Chặn bạn bè:
    @Override
    public void blockFriend(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        if (currentUser.getId().equals(friendUserId)) {
            throw new IllegalArgumentException("Bạn không thể chặn chính mình.");
        }
        Optional<Friend> isFriend = friendRepository.findFriendRelation(currentUser.getId(), friendUserId);
        Friend friend;
        if (isFriend.isPresent()) {// Nếu mối quan hệ tồn tại
            friend = isFriend.get();
            friend.setStatus(FriendshipStatus.BLOCKED);
            friend.setUpdatedAt(LocalDateTime.now());
        } else {
            //Mối quan hện không tồn tjai
            User friendUser = userRepository.findById(friendUserId)
                    .orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại."));
            friend = Friend.builder()
                    .user(currentUser)
                    .friendUser(friendUser)
                    .createdAt(LocalDateTime.now())
                    .status(FriendshipStatus.BLOCKED)
                    .build();
        }
        friendRepository.save(friend);
    }
    // Bỏ chặn bạn bè
    @Override
    public void unblockFriend(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        if (currentUser.getId().equals(friendUserId)) {
            throw new IllegalArgumentException("Bạn không thể bỏ chặn chính mình.");
        }
        userRepository.findById(friendUserId).orElseThrow(() -> new NoSuchElementException("người dùng khoogn tồn tại"));
        // Kiểm tra mối quan hệ giữa hai người
        Friend friend = friendRepository.findByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.BLOCKED)
                .orElseThrow(() -> new RuntimeException("Người này chưa bị chặn."));
        friendRepository.delete(friend);
    }

    // từ chối lời mời kết bạn
    @Override
    public void declineFriendRequest(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        Friend friendSender = friendRepository.findByUserIdAndFriendUserIdAndStatus( friendUserId,currentUser.getId(), FriendshipStatus.PENDING)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy lời mời kết bạn."));
        friendSender.setStatus(FriendshipStatus.DECLINED);
        friendSender.setUpdatedAt(LocalDateTime.now());
        friendRepository.save(friendSender);
    }

    // danh sách friend chờ xác nhận bạn
    @Override
    public List<UserRegisterResponseDTO> findPendingFriends() {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        List<User> friends= friendRepository.findAllByFriendUserIdAndStatus(currentUser.getId(), FriendshipStatus.PENDING);
        return friends.stream()
                .map(this:: convertFromUserToUserRegisterResponseDTO)
                .collect(Collectors.toList());
    }

    // danh sách người đã bị chặn
    @Override
    public List<UserRegisterResponseDTO> findBlockedFriends() {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        List<User> userList= friendRepository.findAllByUserIdAndStatus(currentUser.getId(), FriendshipStatus.BLOCKED);
        return userList.stream()
                .map(this:: convertFromUserToUserRegisterResponseDTO)
                .collect(Collectors.toList());
    }

    // danh sách gợi ý kết bạn dựa vào bạn bè chung (hiển thị số lượng bạn chung)
    @Override
    public List<UserRegisterResponseDTO> suggestFriendsBasedOnCommonFriends() {
        // 1. Lấy tất cả bạn bè hiện tại (mọi trạng thái: ACCEPTED, REQUESTED, PENDING, etc.) = 2 chiều
        //Loại bỏ những người đã là bạn hoặc chính người dùng.
        //Tính toán số lượng bạn chung giữa người dùng hiện tại và các ứng viên gợi ý.
        //Trả về danh sách gợi ý bạn bè kèm số lượng bạn chung
        User currentUser = userServiceImpl.getCurrentUserInfo();
        List<User> currentUserFriendList = friendRepository.findAllByUserId(currentUser.getId());

        Set<Long> idOfCurrentUserFriendList = currentUserFriendList.stream()// tập hợp ID bạn bè cua User hien tai  để dễ kiểm tra
                .map((BaseObject::getId))
                .collect(Collectors.toSet());

        List<SuggestedFriendDTO> suggestedFriendDTOList = new ArrayList<>(); // Danh sách để lưu trữ các gợi ý kết bạn
        for(User friendUser : currentUserFriendList) {   // Lặp qua từng người bạn của người dùng hiện tại
            List<User> listFriendUserOfFriend = friendRepository.findAllByFriendUserIdAndStatus(friendUser.getId(),FriendshipStatus.ACCEPTED);// Lấy danh sách bạn bè của bạn đó
            for (User friendOfFriend : listFriendUserOfFriend) {
                //kiểm tra ID ( 1 ng bạn của bạn bè ) đã tồn tại trong danh sách Id bạn bè hiện tại của mình chưa
                if(!idOfCurrentUserFriendList.contains(friendOfFriend.getId())
                        && !friendOfFriend.getId().equals(currentUser.getId())) {
                    long commonFriendCount= friendRepository.countCommonFriends(currentUser.getId(), friendOfFriend.getId());
                    // them vao dnah sach goi y
                    suggestedFriendDTOList.add(new SuggestedFriendDTO(friendOfFriend, commonFriendCount));
                }
            }
        }
        // Chuyển đổi danh sách SuggestedFriendDTO thành UserRegisterResponseDTO
        return suggestedFriendDTOList.stream()
                .map(this::convertToUserRegisterResponseDTO) // Gọi phương thức chuyển đổi đối tượng
                .collect(Collectors.toList());
    }


    // Phương thức chuyển đổi một đối tượng SuggestedFriendDTO thành UserRegisterResponseDTO
    private UserRegisterResponseDTO convertToUserRegisterResponseDTO(SuggestedFriendDTO suggestedFriendDTO) {
        User suggestedUser = suggestedFriendDTO.getSuggestedUser();
        UserInfo userInfo= userInfoRepository.findByUserId(suggestedUser.getId()).orElseThrow(()->new NoSuchElementException("khong tim thay userInfo"));
        return UserRegisterResponseDTO.builder()
                .id(suggestedUser.getId())
                .username(suggestedUser.getFirstName()+" "+suggestedUser.getLastName())
                .email(suggestedUser.getEmail())
                .phone(suggestedUser.getPhone())
                .status(suggestedUser.getStatus())
                .roles(suggestedUser.getRoles())
                .info(userInfo)
                .suggestedFriendDTO(suggestedFriendDTO)
                .build();
    }

    // danh sách gợi ý kết bạn bạn bè dựa vào  (trong cùng 1 group)
    @Override
    public List<UserRegisterResponseDTO> suggestFriendsBasedOnInterestGroup() {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        // 1. Lấy danh sách tất cả các nhóm mà người dùng hiện tại tham gia
        List<Long> groupIds = groupDetailRepository.findByMemberId(currentUser.getId()).stream()
                .map(groupDetail -> groupDetail.getGroup().getId())
                .toList();
        // 2. Lấy danh sách thành viên trong tất cả các nhóm
        List<GroupDetail> allGroupMembers = groupDetailRepository.findByGroupIdIn(groupIds);
        // 3. Lọc danh sách thành viên tiềm năng
        List<User> potentialFriends = allGroupMembers.stream()
                .map(GroupDetail::getMember) // Loại bỏ chính mình
                .filter(member -> !member.getId().equals(currentUser.getId()))
                .filter(member -> !friendRepository.existsByUserIdAndFriendUserIdAndStatus(
                        currentUser.getId(),
                        member.getId(),
                        FriendshipStatus.ACCEPTED)) // Loại bỏ bạn bè
                .distinct() // Loại bỏ các thành viên trùng lặp
                .toList();
        return potentialFriends.stream()
                .map(this:: convertFromUserToUserRegisterResponseDTO)
                .collect(Collectors.toList());
    }


    // Phương thức chuyển đổi User sang UserRegisterResponseDTO
    private UserRegisterResponseDTO convertFromUserToUserRegisterResponseDTO(User user){
        User currentUser = userServiceImpl.getCurrentUserInfo();
        SuggestedFriendDTO suggestedFriendDTO = SuggestedFriendDTO.builder()
                .commonFriendsCount(friendRepository.countCommonFriends(currentUser.getId(), user.getId()))
                .suggestedUser(user)
                .build();
        UserInfo userInfo = userInfoRepository.findByUserId(user.getId()).orElseThrow(()->new NoSuchElementException("khong tim thay userInfo"));
        return UserRegisterResponseDTO.builder()
                .id(user.getId())
                .username(user.getFirstName()+" "+user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .roles(user.getRoles())
                .info(userInfo)
                .suggestedFriendDTO(suggestedFriendDTO)
                .build();
    }

    @Override
    public UserRegisterResponseDTO findFriendById(Long friendId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();
        Friend friend = friendRepository.findByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendId,FriendshipStatus.ACCEPTED).orElseThrow(()->new NoSuchElementException("Id người bạn không tồn tại"));
        User user= friend.getUser();
        return authServiceImpl.userConvertToResponseDTO(user);
    }

// không tối ưu bằng GPT
    // tìm trạng thái giữa người userLogin và người user khác
//    public Integer findStatusByUserLoginAndUser (Long friendUserId){
//        User currentUser = userServiceImpl.getCurrentUserInfo();
//        // người login là người chủ động gửi lời mời
//        if(friendRepository.existsByUserIdAndFriendUserIdAndStatus(currentUser.getId(), friendUserId, FriendshipStatus.PENDING)) {
//            return 1;
//        // người khác gửi lời mời cho người userLogin
//        }if(friendRepository.existsByUserIdAndFriendUserIdAndStatus(friendUserId,currentUser.getId(), FriendshipStatus.PENDING)){
//            return 2;
//        //đang là bạn bè
//        }if(friendRepository.existsByUserIdAndFriendUserIdAndStatus(friendUserId,currentUser.getId(), FriendshipStatus.ACCEPTED)){
//            return 3;
//        // đã từ chối
//        }if(friendRepository.existsByUserIdAndFriendUserIdAndStatus(friendUserId,currentUser.getId(), FriendshipStatus.DECLINED)){
//            return 4;
//        // đã block
//        }if(friendRepository.existsByUserIdAndFriendUserIdAndStatus(friendUserId,currentUser.getId(), FriendshipStatus.BLOCKED)){
//            return 5;
//        }
//            return 0;// không có mối quan hệ bạn bè
//    }


    // Tìm trạng thái giữa người userLogin và người user khác
    @Override
    public Integer findStatusByUserLoginAndUser(Long friendUserId) {
        User currentUser = userServiceImpl.getCurrentUserInfo();

        // Lấy tất cả các mối quan hệ liên quan đến currentUser và friendUserId
        Optional<Friend> relationship = friendRepository.findByUserIdAndFriendUserId(currentUser.getId(), friendUserId);

        if (relationship.isPresent()) {
            FriendshipStatus status = relationship.get().getStatus();

            // Phân tích trạng thái
            switch (status) {
                case PENDING:
                    // Người dùng hiện tại đã gửi lời mời
                    return 1;
                case ACCEPTED:
                    // Hai người đã là bạn bè
                    return 3;
                case DECLINED:
                    // Đã bị từ chối
                    return 4;
                case BLOCKED:
                    // Đã bị chặn
                    return 5;
            }
        }
        // Kiểm tra xem người kia gửi lời mời cho người dùng hiện tại
        Optional<Friend> reverseRelationship = friendRepository.findByUserIdAndFriendUserId(friendUserId, currentUser.getId());
        if (reverseRelationship.isPresent() && reverseRelationship.get().getStatus() == FriendshipStatus.PENDING) {
            return 2; // Người khác gửi lời mời
        }
        // Nếu không có bất kỳ mối quan hệ nào
        return 0;
    }


}
