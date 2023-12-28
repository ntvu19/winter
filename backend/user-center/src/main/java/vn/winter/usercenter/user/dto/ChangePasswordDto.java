package vn.winter.usercenter.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String password;

    @NotBlank
    private String verifiedPassword;
}
