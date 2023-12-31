package vn.winter.usercenter.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.winter.usercenter.user.dto.*;

import java.util.List;

@RestController()
@RequestMapping("api/v1/user-center/user")
@Validated
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

    @PatchMapping("/active-user")
    public ResponseEntity<Object> activeUser(@Valid @RequestBody ActiveUserDto activeUserDto) {
        return this.userService.activeUser(activeUserDto);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Object> resendOTP(@Valid @RequestBody ResendOtpDto resendOtpDto) {
        return this.userService.resendOTP(resendOtpDto);
    }

    @PatchMapping("/forget-password")
    public ResponseEntity<Object> forgetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
        return this.userService.forgetPassword(forgetPasswordDto);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        return this.userService.changePassword(changePasswordDto);
    }

    @PatchMapping("/change-profile")
    public ResponseEntity<Object> changeProfile(@Valid @RequestBody ChangeProfileDto changeProfileDto) {
        return this.userService.changeProfile(changeProfileDto);
    }

    // ...
}
