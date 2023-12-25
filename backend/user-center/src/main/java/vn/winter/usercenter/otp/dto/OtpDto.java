package vn.winter.usercenter.otp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpDto {
    private long otpId;
    private String code;
    private long userId;
    private String type;
    private int remainTime;
    private LocalDateTime expiredAt;
    private boolean isUsed;
}
