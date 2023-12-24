package vn.winter.usercenter.otp;

import jakarta.persistence.*;
import lombok.*;
import vn.winter.usercenter.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {
    @Id
    @Column(name = "otp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long otpId;
    private String code;
    private String type;
    @Column(name = "remain_time")
    private int remainTime;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
