package com.ra.projectmd05.model.dto.response;

import com.ra.projectmd05.model.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SuggestedFriendDTO {
    private User suggestedUser;
    private long commonFriendsCount;
}
