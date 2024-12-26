package com.ra.projectmd05.repository;

import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.model.dto.request.UserDetailDTO;
import com.ra.projectmd05.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);

    Page<User> findAllByEmailContaining(String email, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.verifyCode IS NOT NULL AND u.status = :status")
    List<User> findUnverifiedUsers(@Param("status") UserStatus status);

    @Query("select u from User u where lower(concat(u.firstName,' ',u.lastName)) like lower(concat('%', :userName,'%') )  ")
    List<User> findByUserNameContainingIgnoreCase(String userName);






    // để phân trang
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE %:userName%")
    Page<User> findByUserNameContaining(String userName, Pageable pageable);
    //-------------------N start---------------------

    @Query("SELECT new com.ra.projectmd05.model.dto.request.UserDetailDTO(u.id, u.firstName, u.lastName, u.email,u.phone,u.gender,u.status) " +
            "FROM User u WHERE " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) or " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<UserDetailDTO> searchUsers(@Param("keyword") String keyword);


    List<User> findByStatus(UserStatus status);
}
