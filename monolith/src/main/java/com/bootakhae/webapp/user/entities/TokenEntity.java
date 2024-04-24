package com.bootakhae.webapp.user.entities;

import com.bootakhae.webapp.user.dto.TokenDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "tokens")
@Slf4j
@Getter
@NoArgsConstructor
public class TokenEntity {

    @Builder
    public TokenEntity(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "refresh_token")
    private String refreshToken;

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenDto entityToDto(){
        return TokenDto.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
    }
}
