package com.ra.projectmd05.controller.user;

import com.ra.projectmd05.model.dto.response.SuggestedFriendDTO;
import com.ra.projectmd05.model.dto.response.UserRegisterResponseDTO;
import com.ra.projectmd05.model.entity.Friend;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.service.friend.FriendService;
import com.ra.projectmd05.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/friends")
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    // Gửi yêu cầu kết bạn
    @PostMapping("/send-request/{friendUserId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long friendUserId) {
        Friend friend = friendService.sendFriendRequest(friendUserId);
        return new ResponseEntity<>(friend, HttpStatus.CREATED);
    }

    // Hiển thị danh sách bạn bè
    @GetMapping("/friendList")
    public ResponseEntity<?> findFriends() {
        List<UserRegisterResponseDTO> friends = friendService.findFriends();
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    // Chấp nhận lời mời kết bạn
    @PostMapping("/accept-request/{friendUserId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long friendUserId) {
        friendService.acceptFriendRequest(friendUserId);
        return new ResponseEntity<>("Chấp nhận lời mời kết bạn thành công",HttpStatus.OK);
    }

    // Hủy lời mời kết bạn
    @DeleteMapping("/cancel-request/{friendUserId}")
    public ResponseEntity<?> cancelFriendRequest(@PathVariable Long friendUserId) {
        friendService.cancelFriendRequest(friendUserId);
        return new ResponseEntity<>("đã xóa lời mời kết bạn",HttpStatus.OK);
    }

    // Xóa bạn
    @DeleteMapping("/remove-friend/{friendUserId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long friendUserId) {
        friendService.removeFriend(friendUserId);
        return new ResponseEntity<>("đã Xóa bạn ",HttpStatus.OK);
    }

    // Chặn bạn bè
    @PostMapping("/block/{friendUserId}")
    public ResponseEntity<?> blockFriend(@PathVariable Long friendUserId) {
        friendService.blockFriend(friendUserId);
        return new ResponseEntity<>("đã Chặn bạn bè ",HttpStatus.OK);
    }

    // Bỏ chặn bạn bè
    @PostMapping("/unblock/{friendUserId}")
    public ResponseEntity<?> unblockFriend(@PathVariable Long friendUserId) {
        friendService.unblockFriend(friendUserId);
        return new ResponseEntity<>("đã Bỏ chặn bạn bè ",HttpStatus.OK);
    }

    // Từ chối lời mời kết bạn
    @DeleteMapping("/decline-request/{friendUserId}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Long friendUserId) {
        friendService.declineFriendRequest(friendUserId);
        return new ResponseEntity<>(" đã Từ chối lời mời kết bạn",HttpStatus.OK);
    }

    // Danh sách bạn bè chờ xác nhận
    @GetMapping("/pending-friends")
    public ResponseEntity<?> findPendingFriends() {
        List<UserRegisterResponseDTO> pendingFriends = friendService.findPendingFriends();
        return new ResponseEntity<>(pendingFriends, HttpStatus.OK);
    }

    // Danh sách người đã bị chặn
    @GetMapping("/blocked-friends")
    public ResponseEntity<?> findBlockedFriends() {
        List<UserRegisterResponseDTO> blockedFriends = friendService.findBlockedFriends();
        return new ResponseEntity<>(blockedFriends, HttpStatus.OK);
    }


    // Gợi ý kết bạn dựa trên bạn bè chung (hiển thị số lượng bạn chung)
    @GetMapping("/suggestions/common-friends")
    public ResponseEntity<?> suggestFriendsBasedOnCommonFriends() {
        List<UserRegisterResponseDTO> suggestedFriends = friendService.suggestFriendsBasedOnCommonFriends();
        return new ResponseEntity<>(suggestedFriends, HttpStatus.OK);
    }

    // Gợi ý kết bạn dựa trên  cùng một nhóm
    @GetMapping("/suggestions/interest-group")
    public ResponseEntity<?> suggestFriendsBasedOnInterestGroup() {
        List<UserRegisterResponseDTO> suggestedFriends = friendService.suggestFriendsBasedOnInterestGroup();
        return new ResponseEntity<>(suggestedFriends, HttpStatus.OK);
    }

    //lấy ra bạn bè dựa vào Id
    @GetMapping("/getFriend/{friendUserId}")
    public ResponseEntity<?> getFriend(@PathVariable Long friendUserId) {
        UserRegisterResponseDTO friend = friendService.findFriendById(friendUserId);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }

    //tìm người dùng dựa vòa tên
    @GetMapping("/search")
    public ResponseEntity<?> findFriendsByName(@RequestParam String searchName) {
        List<User> userList = userService.searchUsersByName(searchName);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    // lấy id người dùng đang đăng nhập
    @GetMapping("/userLogin")
    public ResponseEntity<?> getCurrentUserInfo(){
        Long CurrentUserId= userService.getCurrentUserInfo().getId();
        return new ResponseEntity<>(CurrentUserId, HttpStatus.OK);
    }
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> findFriendById(@PathVariable Long userId) {
       Integer statusNumber= friendService.findStatusByUserLoginAndUser(userId);
       return new ResponseEntity<>(statusNumber, HttpStatus.OK);
    }



}
