package vn.winter.usercenter.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "created_at", columnDefinition = "timestamptz default now()")
    private Date createdAt;
    @Column(name = "updated_at", columnDefinition = "timestamptz default now()")
    private Date updatedAt;
    @Column(name = "plan_id")
    private long planId;


    public boolean getDeletedStatus() {
        return this.isDeleted;
    }

    public boolean getBlockedStatus() {
        return this.isBlocked;
    }
}
