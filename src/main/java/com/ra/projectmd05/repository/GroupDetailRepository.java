package com.ra.projectmd05.repository;

import com.ra.projectmd05.constants.GroupRole;
import com.ra.projectmd05.model.entity.GroupDetail;
import com.ra.projectmd05.model.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupDetailRepository extends JpaRepository<GroupDetail, Long> {
    //--------------------- YẾN -START---------------------------
    // Lấy danh sách các nhóm mà người dùng hiện tại tham gia
    @Query("SELECT gd FROM GroupDetail gd WHERE gd.member.id = :memberId")
    List<GroupDetail> findByMemberId(Long memberId);

    // Lấy danh sách các thành viên trong danh sách nhóm cụ thể
    @Query("SELECT gd FROM GroupDetail gd WHERE gd.group.id IN :groupIds")
    List<GroupDetail> findByGroupIdIn(List<Long> groupIds);

    // Lấy danh sách các thành viên của một nhóm cụ thể
    @Query("SELECT gd FROM GroupDetail gd WHERE gd.group.id = :groupId")
    List<GroupDetail> findByGroupId(Long groupId);

    //----------------------YẾN-END---------------------------


    //----------------------NGHĨA-START---------------------------

    //----------------------NGHĨA-END---------------------------
    //----------------------HÙNG START----------------------------
    @Modifying
    @Query("DELETE from GroupDetail gr where gr.group.id = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId );
    @Query("SELECT COUNT(gr) FROM GroupDetail gr WHERE gr.group.id = :groupId")
    Long countMembersByGroupId(@Param("groupId") Long groupId);
    boolean existsByGroupIdAndMember_Id(Long groupId, Long memberId) throws DataAccessException;
    // rời nhóm
    @Modifying
    @Query("DELETE FROM GroupDetail gr WHERE gr.group.id = :groupId AND gr.member.id = :memberId")
    void leaveGroup(@Param("groupId") Long groupId, @Param("memberId") Long memberId);
    // SupperAdmin
    boolean existsByGroupIdAndMember_IdAndRole(Long groupId, Long memberId, GroupRole role) ;
    Optional<GroupDetail> findByGroupIdAndMemberId(Long groupId, Long memberId);
    //----------------------HÙNG END----------------------------
}
