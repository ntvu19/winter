package vn.winter.usercenter.user;

import vn.winter.usercenter.user.dto.UserDto;

public class UserMapper {
    private static UserMapper INSTANCE;

    public static UserMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }

    public UserDto toDTO(User user) {
        UserDto dto = new UserDto();

        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setDeleted(user.isDeleted());
        dto.setBlocked(user.isBlocked());

        return dto;
    }
}
