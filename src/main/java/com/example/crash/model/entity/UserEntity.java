package com.example.crash.model.entity;

import com.example.crash.model.user.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table( //"user" 이렇게 작성하는 이유는 user는 데이터베이스에 예약어이기 때문에 이렇게 작성
        // Postgresql 에서 쿼리를 칠려고 user를 조회하면 계속 에약어 조회되기 때문에 users로 수정
        name="\"users\"",
        indexes = { // username 을 자주 호출할거여서 이렇게 인덱스 생성 , 그리고 이렇게 유니크를 걸으므로 db에서 중복을 방지 가능
                @Index(name="users_username_idx",columnList = "username" ,unique = true)
        }
)
@Setter
@Getter
public class UserEntity implements UserDetails {

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


    //로그인한 사용자의 권한을 조회하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(
//                //1번방식
//                new SimpleGrantedAuthority(Role.USER.name()), //"USER" 문자열로 이렇게 들어간다 =이렇게 하면 hasAuthority 통과하는데 hasRole은 통과를 못한다
//                new SimpleGrantedAuthority("ROLE_"+Role.USER.name()) // 이렇게 하면 hasRole을 통과 할 수 있다
//
//                //2번 방식
//                new SimpleGrantedAuthority("ROLE_"+Role.ADMIN.name()),
//                new SimpleGrantedAuthority("READ_AUTHORITY"),
//                new SimpleGrantedAuthority("CREATE_AUTHORITY"),
//                new SimpleGrantedAuthority("UPDATE_AUTHORITY"),
//                new SimpleGrantedAuthority("DELETE_AUTHORITY")
//
//        );

        if (this.role.equals(Role.ADMIN)){//어드민 유저
            return
                    List.of(new SimpleGrantedAuthority("ROLE_"+Role.ADMIN.name()),
                            new SimpleGrantedAuthority(Role.ADMIN.name()),
                            new SimpleGrantedAuthority("ROLE_"+Role.USER.name()),
                            new SimpleGrantedAuthority(Role.USER.name()));
        }else { // 일반유저
            return List.of(new SimpleGrantedAuthority("ROLE_"+Role.USER.name()),
                    new SimpleGrantedAuthority(Role.USER.name()));
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
