package vn.winter.usercenter.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.winter.usercenter.util.Util;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInDto implements Serializable {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public void setEmail(String email) {
        this.email = Util.trim(email);
    }

    public void setPassword(String password) {
        this.password = Util.trim(password);
    }

}
