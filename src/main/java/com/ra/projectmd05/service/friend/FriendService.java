package com.ra.projectmd05.service.friend;

import com.ra.projectmd05.model.dto.response.SuggestedFriendDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.Friend;
import com.ra.projectmd05.model.entity.User;

import java.util.List;

public interface FriendService {

    Friend sendFriendRequest(Long friendUserId);
    // hiển thị danh sách bạn bè
    List<UserRegisterResponseDTO> findFriends();
    // Chấp nhận lời mời kết bạn:
    void acceptFriendRequest( Long friendUserId);
    // Hủy lời mời kết bạn:
    void cancelFriendRequest(Long friendUserId);
    // Xóa  bạn:
    void removeFriend(Long friendUserId);
    // Chặn bạn bè:
    void blockFriend(Long friendUserId);
    // Bỏ chặn bạn bè
    void unblockFriend(Long friendUserId);
    // từ chối lời mời kết bạn
    void declineFriendRequest(Long friendUserId);
    // danh sách friend chờ xác nhận bạn
    List<UserRegisterResponseDTO> findPendingFriends();
    // danh sách người đã bị chặn
    List<UserRegisterResponseDTO> findBlockedFriends();
    // danh sách gợi ý kết bạn dựa vào bạn bè chung (hiển thị số lượng bạn chung)
    List<UserRegisterResponseDTO> suggestFriendsBasedOnCommonFriends();

    // danh sách gợi ý kết bạn bạn bè dựa vào  (trong cùng 1 group)
    List<UserRegisterResponseDTO> suggestFriendsBasedOnInterestGroup();

    UserRegisterResponseDTO findFriendById(Long id);

    Integer findStatusByUserLoginAndUser(Long friendUserId);

}
