package vn.winter.usercenter.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/user-center")
public class UserController {
    @GetMapping("hello")
    public String hello() {
        return "Hello Nguyen Thanh Vu";
    }
}
