package com.ra.projectmd05.security;

import com.ra.projectmd05.constants.UserStatus;
import com.ra.projectmd05.model.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPrinciple implements UserDetails {
    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        if (user.getStatus() == UserStatus.ACTIVE) {
            return true;// Nếu trạng thái là ACTIVE, trả về true
        } else if (user.getStatus() == UserStatus.BLOCKED || user.getStatus() == UserStatus.VERIFY) {
            // Nếu trạng thái là BLOCKED hoặc VERIFY, trả về false
            return false;
        }
        // Trường hợp không rõ ràng, mặc định trả về false
        return false;
    }
}
