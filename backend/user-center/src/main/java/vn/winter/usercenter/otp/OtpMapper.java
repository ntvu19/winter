package vn.winter.usercenter.otp;

import vn.winter.usercenter.otp.dto.OtpDto;

public class OtpMapper {
    private static OtpMapper INSTANCE;

    public static OtpMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OtpMapper();
        }
        return INSTANCE;
    }

    public OtpDto toDTO(OTP otp) {
        return OtpDto.builder()
                .otpId(otp.getOtpId())
                .code(otp.getCode())
                .userId(otp.getUser().getUserId())
                .type(otp.getType())
                .remainTime(otp.getRemainTime())
                .expiredAt(otp.getExpiredAt())
                .isUsed(otp.isUsed())
                .build();
    }

}
