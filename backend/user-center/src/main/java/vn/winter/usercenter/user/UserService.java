package vn.winter.usercenter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.winter.usercenter.user.dto.UserSignUpDto;
import vn.winter.usercenter.util.ResponseMap;
import vn.winter.usercenter.util.Util;

import java.util.Date;
import java.util.List;

@Service
@Transactional
    public class UserService {
        @Autowired
        private UserRepository userRepository;
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

        /*
         *  NOTE:
         *  - OTP
         *  - Active status
         *  - Birthday
         */

        // Get request parameters
        String fullName = userSignUpDto.getFullName();
        String email = userSignUpDto.getEmail();
        String password = userSignUpDto.getPassword();
        String phone = userSignUpDto.getPhone();

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
            return new ResponseEntity<>(responseMap.setValue("message", "User exist!")
                .setValue("status", HttpStatus.BAD_REQUEST.value())
                .build(),
                    HttpStatus.BAD_REQUEST);
        }

        // Generate random code
        String randomSalt = Util.generateRandomSaltCode(16);

        // Hashing password + salt
        String hashedPassword = passwordEncoder.encode(password + randomSalt);

        // If user doesn't exist
        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(hashedPassword)
                .phone(phone)
                .salt(randomSalt)
                .planId(1L)                 // Default storage plan (FREE)
                .createdAt(new Date())      // Set created time
                .updatedAt(new Date())      // Set updated time
                .build();

        // Save user to database
        this.userRepository.save(user);

        return new ResponseEntity<>(responseMap.setValue("message", "Success")
                .setValue("data", UserMapper.getInstance().toDTO(user))
                .setValue("status", HttpStatus.CREATED.value())
                .build(),
                    HttpStatus.CREATED);
    }
}