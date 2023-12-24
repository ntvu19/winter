package vn.winter.usercenter.user;

import jakarta.persistence.*;
import lombok.*;
import vn.winter.usercenter.otp.OTP;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private String salt;
    private String phone;
    private LocalDate birthday;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "created_at", columnDefinition = "timestamptz default now()")
    private LocalDateTime createdAt;
    @Column(name = "updated_at", columnDefinition = "timestamptz default now()")
    private LocalDateTime updatedAt;
    @Column(name = "plan_id")
    private long planId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<OTP> otpCollection;
}
