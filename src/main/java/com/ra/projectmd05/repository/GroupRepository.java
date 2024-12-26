package com.ra.projectmd05.repository;

import com.ra.projectmd05.model.entity.Group;
import com.ra.projectmd05.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    //----------------------HÙNG START----------------------------
    Group findByIdAndUser(long id, User user);
    //----------------------HÙNG END----------------------------

}
