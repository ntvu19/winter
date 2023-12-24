package vn.winter.usercenter.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.winter.usercenter.util.Util;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto implements Serializable {
    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    @Past
    private LocalDate birthday;

    public void setFullName(String fullName) {
        this.fullName = Util.trim(fullName);
    }

    public void setEmail(String email) {
        this.email = Util.trim(email);
    }

    public void setPassword(String password) {
        this.password = Util.trim(password);
    }

    public void setPhone(String phone) {
        this.phone = Util.trim(phone);
    }
}
