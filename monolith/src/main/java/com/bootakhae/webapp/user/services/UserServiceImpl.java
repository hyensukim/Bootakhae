package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.mapper.UserMapper;
import com.bootakhae.webapp.user.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;

    @Transactional
    @Override
    public UserDto signup(UserDto userDetails) {
        log.debug("회원 가입 서비스 실행");
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(userDetails);

        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("회원 가입 실패 : 중복된 이메일 입니다.");
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
                .orElseThrow(() -> new RuntimeException("회원 상세 조회 실패 : 존재하지 않는 회원입니다."));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Override
    public UserDto getUserDetailsByEmail(String encryptedEmail) {
        log.debug("JWT 발급 : 이메일로 회원 정보 조회 실행");
        UserEntity user = userRepository.findByEmail(encryptedEmail)
                .orElseThrow(()-> new RuntimeException("JWT 발급 : 가입되지 않은 회원입니다."));
        return UserMapper.INSTANCE.entityToDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUserInfo(UserDto userDetails, String userId) {
        log.debug("회원 정보 수정 실행");
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("회원 정보 수정 실패 : 존재하지 않는 회원입니다."));

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
                .orElseThrow(() -> new RuntimeException("비밀번호 변경 실패 : 존재하지 않는 회원입니다."));

        if(!passwordEncoder.matches(oldPw, userEntity.getPassword())){
            throw new RuntimeException("비밀번호 변경 실패 : 비밀번호가 일치하지 않습니다.");
        }
        else if(!newPw.equals(confirmPw)){
            throw new RuntimeException("비밀번호 변경 실패 : 비밀번호를 다시 확인해주세요");
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

        return new User(userEntity.getEmail(), userEntity.getPassword(),
                true,true,true,true,
                new ArrayList<>());
    }
}
