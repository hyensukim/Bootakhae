package com.bootakhae.webapp.user.entities;

import com.bootakhae.webapp.entities.BaseEntity;
import com.bootakhae.webapp.user.constant.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Builder
    public UserEntity(
            String email,
            String password,
            String address1,
            String address2,
            String name,
            String nickname,
            String phone
    ){
        this.userId = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.address1 = address1;
        this.address2 = address2;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.role = Role.USER;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    
    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "user_email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "user_password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_address1", nullable = false, length = 100)
    private String address1; // 우편 번호

    @Column(name = "user_address2", nullable = false, length = 100)
    private String address2; // 상세주소

    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "user_phone", nullable = false, length = 50)
    private String phone;

    @LastModifiedDate
    @Column(name="updated_at", insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 10)
    private Role role;

    public void changePw(String newPw){
        this.password = newPw;
    }

    public void updateAddOne(String ad){
        if(!this.address1.equals(ad)){
            this.address1 = ad;
        }
    }

    public void updateAddTwo(String ad){
        if(!this.address2.equals(ad)){
            this.address2 = ad;
        }
    }

    public void updatePh(String ph){
        if(!this.phone.equals(ph)){
            this.phone = ph;
        }
    }
}
