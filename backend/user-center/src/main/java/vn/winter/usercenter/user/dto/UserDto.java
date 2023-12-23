package vn.winter.usercenter.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private long userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;
    private boolean isBlocked;
}
