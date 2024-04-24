package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.TokenDto;
import com.bootakhae.userservice.entities.TokenEntity;
import com.bootakhae.userservice.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;

    @Transactional
    @Override
    public void saveRefreshToken(String userId, String refreshToken) {
        log.debug("refresh 토큰 저장 실행");

        tokenRepository.findByUserId(userId).ifPresent(
            token -> {
                token.changeRefreshToken(refreshToken);
            }
        );

        TokenEntity token = TokenEntity.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);
    }

    @Transactional
    @Override
    public void removeRefreshToken(String refreshToken){
        log.debug("refresh 토큰 삭제");
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new RuntimeException("토큰 삭제 : 존재하지 않는 토큰 정보 입니다.")
        );
        tokenRepository.delete(token);
    }

    @Override
    public TokenDto reIssueToken(String refreshToken) {
        if(tokenRepository.existsByRefreshToken(refreshToken)){

        }
        return null;
    }

    @Override
    public TokenDto findTokenByRefreshToken(String refreshToken) {
        log.debug("refresh 토큰으로 토큰 정보 조회");
        TokenEntity token = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new RuntimeException("토큰 정보 조회 : 존재하지 않는 토큰 정보 입니다.")
        );
        return token.entityToDto();
    }

    @Override
    public boolean haveRefreshToken(String refreshToken) {
        log.debug("refresh 토큰 존재 여부 확인");
        return tokenRepository.existsByRefreshToken(refreshToken);
    }
}
