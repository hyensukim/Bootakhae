package com.bootakhae.userservice.services;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.entities.UserEntity;
import com.bootakhae.userservice.global.exception.CustomException;
import com.bootakhae.userservice.global.exception.ErrorCode;
import com.bootakhae.userservice.global.mapper.UserMapper;
import com.bootakhae.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto signup(UserDto userDetails) {
        log.debug("회원 가입 서비스 실행");
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(userDetails);

        if(userRepository.existsByEmail(user.getEmail())){
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        user.changePw(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return UserMapper.INSTANCE.entityToDto(user);
    }

    // todo : token 관련 조회와 일반 조회 구분하여 메서드 정의하기
    @Override
    public UserDto getOneByUserId(String userId) {
        log.debug("회원 상세 조회 실행");
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Override
    public UserDto getUserDetailsByEmail(String encryptedEmail) {
        log.debug("JWT 발급 : 이메일로 회원 정보 조회 실행");
        UserEntity user = userRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUserInfo(UserDto userDetails, String userId) {
        log.debug("회원 정보 수정 실행");
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(Objects.nonNull(userDetails.getAddress1())){
            userEntity.updateAddOne(userDetails.getAddress1());
        }
        if(Objects.nonNull(userDetails.getAddress2())){
            userEntity.updateAddTwo(userDetails.getAddress2());
        }
        if(Objects.nonNull(userDetails.getPhone())){
            userEntity.updatePh(userDetails.getPhone());
        }

        userDetails = UserMapper.INSTANCE.entityToDto(userEntity);

        return userDetails;
    }

    @Transactional
    @Override
    public UserDto updateUserPassword(String userId, String oldPw, String newPw, String confirmPw) {
        log.debug("회원 비밀번호 수정 실행");
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(oldPw, userEntity.getPassword())){
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
        else if(!newPw.equals(confirmPw)){
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
        else{
            userEntity.changePw(passwordEncoder.encode(newPw));
            return UserMapper.INSTANCE.entityToDto(userEntity);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().name()));

        return new User(userEntity.getEmail(), userEntity.getPassword(),
                true,true,true,true,
                authorities);
    }
}
