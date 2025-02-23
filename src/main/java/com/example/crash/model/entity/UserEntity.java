package com.example.crash.model.entity;

import com.example.crash.model.user.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table( //"user" 이렇게 작성하는 이유는 user는 데이터베이스에 예약어이기 때문에 이렇게 작성
        name="\"user\""
)
@Setter
@Getter
public class UserEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column
    private ZonedDateTime createdDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(username, that.username)
                && Objects.equals(password, that.password)
                && Objects.equals(name, that.name)
                && Objects.equals(email, that.email)
                && role == that.role
                && Objects.equals(createdDateTime, that.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, name, email, role, createdDateTime);
    }

    //userentity 로 변환
    public static UserEntity of(String username, String password, String name, String email) {
        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setRole(Role.USER); // 일반 사용자는 유저권한을 준다
        return userEntity;
    }

    @PrePersist //Jpa를통해서 데이터베이스에 데이터가 저장되기 직전에 현재 시간을 필드에 넣어준다
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }
}
