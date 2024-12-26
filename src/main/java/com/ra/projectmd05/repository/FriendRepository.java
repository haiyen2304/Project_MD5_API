package com.ra.projectmd05.repository;

import com.ra.projectmd05.constants.FriendshipStatus;
import com.ra.projectmd05.model.entity.Friend;
import com.ra.projectmd05.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    //----------------------YẾN-START---------------------------

    // check cặp bạn bè đã tồn tại trong danh sách (với 3 trạng thái)
    boolean existsByUserIdAndFriendUserId(Long userId, Long friendId);

    //check xem cos phai ban be hay khong
    boolean existsByUserIdAndFriendUserIdAndStatus(Long userId, Long friendUserId, FriendshipStatus status);
    // lấy ra trạng thái của 2 người
    Optional<Friend> findByUserIdAndFriendUserIdAndStatus(Long currentUserId, Long friendUserId, FriendshipStatus status);
    Friend findByFriendUserIdAndUserIdAndStatus(Long friendUserId, Long userId, FriendshipStatus status);
    Optional<Friend> findByUserIdAndFriendUserId(Long userId, Long friendId);

    //tìm trong bảng Friend  ở côt userID và friendID , trong 2 cột này lấy ra các bản ghi == IDUserLogin và có status == Accepted
    @Query("select f.friendUser from Friend f where f.user.id = :userId and f.status = :status")
    List<User> findAllByUserIdAndStatus(@Param("userId") Long userId,@Param("status") FriendshipStatus status);

    @Query("select f.user from Friend f where f.friendUser.id = :friendUserId and f.status = :status")
    List<User> findAllByFriendUserIdAndStatus(@Param("friendUserId") Long friendUserId,@Param("status") FriendshipStatus status);

    //danh sách bạn bè ở mọi trạng thái  //UNION Kết hợp cả hai trường hợp trên, đảm bảo không trùng lặp.
    @Query("SELECT f.friendUser FROM Friend f WHERE f.user.id = :userId " +
            "UNION " +
            "SELECT f.user FROM Friend f WHERE f.friendUser.id = :userId")
    List<User> findAllByUserId(@Param("userId") Long userId);

    //1. Truy vấn để tìm một bản ghi duy nhất nếu tồn tại
    @Query("SELECT f FROM Friend f WHERE (f.user.id = :userId AND f.friendUser.id = :friendId) " +
            "OR (f.user.id = :friendId AND f.friendUser.id = :userId)")
    Optional<Friend> findFriendRelation(@Param("userId") Long userId, @Param("friendId") Long friendId);


    @Query("""
    SELECT f.user FROM Friend f WHERE f.friendUser.id = :currentUserId AND f.status = 'PENDING'""")
    List<User> findPendingFriends(@Param("currentUserId") Long currentUserId);

    @Query("""
    SELECT f.friendUser FROM Friend f WHERE f.user.id = :currentUserId AND f.status = 'BLOCKED'""")
    List<User> findBlockedFriends(@Param("currentUserId") Long currentUserId);

// Đếm số bản ghi CỦA kết quả TRUY VÂN
//Friend f1: danh sách bạn bè của currentUserId.
//Friend f2:  danh sách bạn bè của NGƯỜI BẠN ĐÓ otherUserId.

    @Query("""
    SELECT COUNT(*) FROM Friend f1 JOIN Friend f2 ON f1.friendUser.id = f2.friendUser.id
    WHERE f1.user.id = :currentUserId AND f2.user.id = :otherUserId AND f1.status = 'ACCEPTED' AND f2.status = 'ACCEPTED'
""")
    long countCommonFriends(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);


}


//DISTINCT: Đảm bảo mỗi thành viên chỉ xuất hiện một lần trong kết quả, tránh bị lặp.




//----------------------YẾN-END---------------------------



//----------------------NGHĨA-START---------------------------

//----------------------NGHĨA-END---------------------------

//----------------------HÙNG START----------------------------

//----------------------HÙNG END----------------------------
