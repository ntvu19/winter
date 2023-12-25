package vn.winter.usercenter.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.winter.usercenter.user.User;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    OTP findOneByUserAndType(User user, String type);
}
