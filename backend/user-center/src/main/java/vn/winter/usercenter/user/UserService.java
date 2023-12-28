package vn.winter.usercenter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.winter.usercenter.otp.OTP;
import vn.winter.usercenter.otp.OtpMapper;
import vn.winter.usercenter.otp.OtpRepository;
import vn.winter.usercenter.user.dto.*;
import vn.winter.usercenter.util.OtpType;
import vn.winter.usercenter.util.ResponseMap;
import vn.winter.usercenter.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
    public class UserService {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private OtpRepository otpRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        private final ResponseMap responseMap;

    public UserService() {
        responseMap = new ResponseMap();
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public ResponseEntity<Object> signUp(UserSignUpDto userSignUpDto) {

        /**
         * NOTE:
         * - Send email
         * - 500 try - catch - exception
         */

        // Get request parameters
        String fullName = userSignUpDto.getFullName();
        String email = userSignUpDto.getEmail();
        String password = userSignUpDto.getPassword();
        String phone = userSignUpDto.getPhone();
        LocalDate birthday = userSignUpDto.getBirthday();

        // Check null request
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email must not be empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check exist user
        User checkedUser = this.userRepository.findOneByEmail(email);

        if (checkedUser != null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User has been existed!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Generate random code
        String randomSalt = Util.generateRandomSalt(16);

        // Hashing password + salt
        String hashedPassword = passwordEncoder.encode(password + randomSalt);

        // If user doesn't exist
        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(hashedPassword)
                .phone(phone)
                .birthday(birthday)
                .salt(randomSalt)
                .planId(1L)                         // Default storage plan (FREE)
                .createdAt(LocalDateTime.now())     // Set created time
                .updatedAt(LocalDateTime.now())     // Set updated time
                .build();

        // Save user to database
        this.userRepository.save(user);

        // Generate register OTP
        OTP otpEntity = OTP.builder()
                .code(Util.generateRandomOtp(6))
                .type(OtpType.REGISTER.toString())
                .remainTime(5 * 60)      // 5 minutes * 60 seconds
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        // Save OTP to database
        this.otpRepository.save(otpEntity);

        // Send an email to user
        // ...

        return new ResponseEntity<>(responseMap
                .setMessage("Success")
                .setStatus(HttpStatus.CREATED.value())
                .setData(UserMapper.getInstance().toDTO(user))
                .build(),
                    HttpStatus.CREATED);
    }

    public ResponseEntity<Object> signIn(UserSignInDto userSignInDto) {

        /**
         * NOTE:
         * - Check active user
         * - Cache the token for Redis
         * -
         */

        // Get email and password
        String email = userSignInDto.getEmail();
        String password = userSignInDto.getPassword();

        // Check the email is blank
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email or password must not be empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check the email in database
        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email or password are wrong!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check password
        String salt = user.getSalt();
        String rawPassword = password + salt;
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email or password are wrong!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Generate access token and refresh token (JWT)


        return new ResponseEntity<>(responseMap
                .setMessage("Login successfully!")
                .setStatus(HttpStatus.OK.value())
                .setData(UserMapper.getInstance().toDTO(user))
                .build(),
                    HttpStatus.OK);
    }

    public ResponseEntity<Object> activeUser(ActiveUserDto activeUserDto) {
        // Get parameters
        String email = activeUserDto.getEmail();
        String otp = activeUserDto.getOtp();

        // Check empty email
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email must be not empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check OTP is equal to "REGISTER"
        if (!Objects.equals(otp, OtpType.REGISTER.toString())) {
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP type must be REGISTER!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check user in database
        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User doesn't exist!")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);
        }

        // Check user has been activated
        if (user.isActive()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User has been activated!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check OTP has been used or has been expired
        OTP checkedOtp = this.otpRepository.findOneByUserAndType(user, OtpType.REGISTER.toString());

        if (checkedOtp == null)
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP doesn't exist!")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);

        if (checkedOtp.isUsed() || checkedOtp.getExpiredAt().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP has been used or has been expired!")
                    .setStatus(HttpStatus.GONE.value())
                    .build(),
                        HttpStatus.GONE);

        // Active user
        if (checkedOtp.getCode().equals(otp)) {
            // Set "is_active" to true
            user.setActive(true);

            // Set "is_used" to true
            checkedOtp.setUsed(true);

            // Save to database
            this.userRepository.save(user);
            this.otpRepository.save(checkedOtp);
        }

        return new ResponseEntity<>(responseMap
                .setMessage("Activate user successfully!")
                .setStatus(HttpStatus.OK.value())
                .build(),
                    HttpStatus.OK);
    }

    public ResponseEntity<Object> resendOTP(ResendOtpDto resendOtpDto) {
        // Get parameter
        String email = resendOtpDto.getEmail();
        OtpType otpType = resendOtpDto.getOtpType();

        // Check null request
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email is empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check user exist
        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User doesn't exist")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);
        }

        // Search OTP
        OTP checkedOtp = this.otpRepository.findOneByUserAndType(user, otpType.toString());

        if (checkedOtp != null) {
            // Set new OTP code and expired time
            checkedOtp.setCode(Util.generateRandomOtp(6));
            checkedOtp.setRemainTime(5 * 60);
            checkedOtp.setExpiredAt(LocalDateTime.now().plusMinutes(10));

            // Update to database
            this.otpRepository.save(checkedOtp);

            // Response successful status
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP has been re-created!")
                    .setStatus(HttpStatus.OK.value())
                    .setData(OtpMapper.getInstance().toDTO(checkedOtp))
                    .build(),
                        HttpStatus.OK);
        }

        // Re-create OTP
        OTP otp = OTP.builder()
                .code(Util.generateRandomOtp(6))
                .type(otpType.toString())
                .remainTime(5 * 60)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        // Save to database
        this.otpRepository.save(otp);

        // Response successful status
        return new ResponseEntity<>(responseMap
                .setMessage("OTP has been re-created!")
                .setStatus(HttpStatus.OK.value())
                .setData(OtpMapper.getInstance().toDTO(otp))
                .build(),
                    HttpStatus.OK);
    }

    public ResponseEntity<Object> forgetPassword(ForgetPasswordDto forgetPasswordDto) {
        // Get parameters
        String email = forgetPasswordDto.getEmail();
        String otp = forgetPasswordDto.getOtp();
        String password = forgetPasswordDto.getPassword();
        String verifiedPassword = forgetPasswordDto.getVerifiedPassword();

        // Check blank email
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email must not be empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check password
        if (!password.equals(verifiedPassword)) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Password must be matched!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check user
        User checkedUser = this.userRepository.findOneByEmail(email);

        if (checkedUser == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User doesn't exist!")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);
        }

        // Check OTP type
        OTP checkedOtp = this.otpRepository.findOneByUserAndType(checkedUser, OtpType.FORGET_PASSWORD.toString());

        if (checkedOtp == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP doesn't exist!")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);
        }

        // Check time and usage status
        if (checkedOtp.isUsed() || checkedOtp.getExpiredAt().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP has been used or has been expired!")
                    .setStatus(HttpStatus.GONE.value())
                    .build(),
                        HttpStatus.GONE);
        }

        // Check matched OTP
        if (!checkedOtp.getCode().equals(otp)) {
            return new ResponseEntity<>(responseMap
                    .setMessage("OTP is not correct!")
                    .setStatus(HttpStatus.NOT_ACCEPTABLE.value())
                    .build(),
                        HttpStatus.NOT_ACCEPTABLE);
        }

        // Hash password
        String salt = checkedUser.getSalt();
        String hashedPassword = passwordEncoder.encode(password + salt);

        // Reset password
        checkedOtp.setUsed(true);
        checkedUser.setPassword(hashedPassword);

        // Save to database
        this.userRepository.save(checkedUser);
        this.otpRepository.save(checkedOtp);

        // Response
        return new ResponseEntity<>(responseMap
                .setMessage("Your password has been reset!")
                .setStatus(HttpStatus.OK.value())
                .build(),
                    HttpStatus.OK);
    }

    public ResponseEntity<Object> changePassword(ChangePasswordDto changePasswordDto) {
        // Get parameters
        String email = changePasswordDto.getEmail();
        String oldPassword = changePasswordDto.getOldPassword();
        String password = changePasswordDto.getPassword();
        String verifiedPassword = changePasswordDto.getVerifiedPassword();

        // Check empty email
        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Email must not be empty!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check matched password
        if (!password.equals(verifiedPassword)) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Password must be matched!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check user
        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(responseMap
                    .setMessage("User doesn't exist!")
                    .setStatus(HttpStatus.NOT_FOUND.value())
                    .build(),
                        HttpStatus.NOT_FOUND);
        }

        // Check old password
        String salt = user.getSalt();
        String encodedOldPassword = user.getPassword();

        if (!passwordEncoder.matches(oldPassword + salt, encodedOldPassword)) {
            return new ResponseEntity<>(responseMap
                    .setMessage("Old password are wrong!")
                    .setStatus(HttpStatus.BAD_REQUEST.value())
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // Hashing password
        String hashedPassword = passwordEncoder.encode(password + salt);

        // Change password
        user.setPassword(hashedPassword);

        // Save to database
        this.userRepository.save(user);

        return new ResponseEntity<>(responseMap
                .setMessage("Password has been changed!")
                .setStatus(HttpStatus.OK.value())
                .build(),
                    HttpStatus.OK);
    }
}






















