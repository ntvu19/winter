package vn.winter.usercenter.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.winter.usercenter.otp.OTP;
import vn.winter.usercenter.user.dto.UserSignInDto;
import vn.winter.usercenter.user.dto.UserSignUpDto;
import vn.winter.usercenter.util.OtpType;
import vn.winter.usercenter.util.Util;

import java.time.LocalDateTime;
import java.util.List;

@RestController()
@RequestMapping("api/v1/user-center/user")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAllUser() {
        return this.userService.getAllUser();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserSignUpDto userSignUpDto) {
        return this.userService.signUp(userSignUpDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn(@Valid @RequestBody UserSignInDto userSignInDto) {
        return this.userService.signIn(userSignInDto);
    }

    // Active user
    // Re-send OTP
    // Forget password
    // Change password
    // Change information
    // ...

}
