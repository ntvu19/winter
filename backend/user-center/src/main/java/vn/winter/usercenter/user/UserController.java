package vn.winter.usercenter.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.winter.usercenter.user.dto.ActiveUserDto;
import vn.winter.usercenter.user.dto.ResendOtpDto;
import vn.winter.usercenter.user.dto.UserSignInDto;
import vn.winter.usercenter.user.dto.UserSignUpDto;

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

    @PatchMapping("/active-user")
    public ResponseEntity<Object> activeUser(@Valid @RequestBody ActiveUserDto activeUserDto) {
        return this.userService.activeUser(activeUserDto);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Object> resendOTP(@Valid @RequestBody ResendOtpDto resendOtpDto) {
        return this.userService.resendOTP(resendOtpDto);
    }

    // Forget password
    // Change password
    // Change information
    // ...

}
