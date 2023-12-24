package vn.winter.usercenter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.winter.usercenter.otp.OTP;
import vn.winter.usercenter.otp.OtpRepository;
import vn.winter.usercenter.user.dto.UserSignInDto;
import vn.winter.usercenter.user.dto.UserSignUpDto;
import vn.winter.usercenter.util.OtpType;
import vn.winter.usercenter.util.ResponseMap;
import vn.winter.usercenter.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
            return new ResponseEntity<>(responseMap.setValue("message", "Email must not be empty!")
                    .setValue("status", HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check exist user
        User checkedUser = this.userRepository.findOneByEmail(email);

        if (checkedUser != null) {
            return new ResponseEntity<>(responseMap.setValue("message", "User is exist!")
                .setValue("status", HttpStatus.BAD_REQUEST.value())
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
                .remainTime(5 * 60 * 1000)      // 5 minutes * 60 seconds * 1000 miliseconds
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        // Save OTP to database
        this.otpRepository.save(otpEntity);

        // Send an email to user
        // ...

        return new ResponseEntity<>(responseMap.setValue("message", "Success")
                .setValue("data", UserMapper.getInstance().toDTO(user))
                .setValue("status", HttpStatus.CREATED.value())
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
                    .setValue("message", "Email or password must not be empty!")
                    .setValue("status", HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check the email in database
        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(responseMap
                    .setValue("message", "Email or password are wrong!")
                    .setValue("status", HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Check password
        String salt = user.getSalt();
        String rawPassword = password + salt;
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            return new ResponseEntity<>(responseMap
                    .setValue("message", "Email or password are wrong!")
                    .setValue("status", HttpStatus.BAD_REQUEST.value())
                    .build(),
                        HttpStatus.BAD_REQUEST);
        }

        // Generate access token and refresh token (JWT)


        return new ResponseEntity<>(responseMap
                .setValue("message", "Success!")
                .setValue("status", HttpStatus.OK.value())
                .setValue("data", UserMapper.getInstance().toDTO(user))
                .build(),
                    HttpStatus.OK);
    }
}
