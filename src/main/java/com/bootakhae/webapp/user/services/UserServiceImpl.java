package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.common.utils.AesCryptoUtil;
import com.bootakhae.webapp.user.dto.CryptoInfoDto;
import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.mapper.UserMapper;
import com.bootakhae.webapp.user.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    // todo: 실제 서비스 시에는 해당값을 고정값으로 지정
    private static final SecretKey SECRETKEY = AesCryptoUtil.getKey();
    private static final IvParameterSpec IV = AesCryptoUtil.getIv();

    private final UserRepository userRepository;
    private final Environment env;

    @Override
    public UserDto signup(UserDto userDetails) {
        log.debug("회원 가입 서비스 실행");
        CryptoInfoDto cryptoInfo = userInfoEncrypt(userDetails);
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(cryptoInfo.getUserDetails());
        user = userRepository.save(user);
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        return userInfoDecrypt(userDto, cryptoInfo.getKey(), cryptoInfo.getIv());
    }

    private CryptoInfoDto userInfoEncrypt(UserDto userDetails){
        try{
            String specName = env.getProperty("aes.spec");

            userDetails.setAddress1(AesCryptoUtil.encrypt(specName,SECRETKEY,IV,userDetails.getAddress1()));
            userDetails.setAddress2(AesCryptoUtil.encrypt(specName,SECRETKEY,IV,userDetails.getAddress2()));
            userDetails.setEmail(AesCryptoUtil.encrypt(specName,SECRETKEY,IV,userDetails.getEmail()));
            userDetails.setName(AesCryptoUtil.encrypt(specName,SECRETKEY,IV,userDetails.getName()));
            userDetails.setPhone(AesCryptoUtil.encrypt(specName,SECRETKEY,IV,userDetails.getPhone()));

            return userDetails;
        }
        catch(Exception e){
            log.error("");
            throw new RuntimeException(e);
        }
    }

    private String userInfoEncrypt(String info){ // 단일 정보 암호화
        try{
            String specName = env.getProperty("aes.spec");

            return AesCryptoUtil.encrypt(specName,SECRETKEY,IV,info);
        }
        catch(Exception e){
            log.error("");
            throw new RuntimeException(e);
        }
    }

    private UserDto userDecrypt(UserDto userDetails){
        try{
            String specName = env.getProperty("aes.spec");

            userDetails.setAddress1(AesCryptoUtil.decrypt(specName,SECRETKEY,IV,userDetails.getAddress1()));
            userDetails.setAddress2(AesCryptoUtil.decrypt(specName,SECRETKEY,IV,userDetails.getAddress2()));
            userDetails.setEmail(AesCryptoUtil.decrypt(specName,SECRETKEY,IV,userDetails.getEmail()));
            userDetails.setName(AesCryptoUtil.decrypt(specName,SECRETKEY,IV,userDetails.getName()));
            userDetails.setPhone(AesCryptoUtil.decrypt(specName,SECRETKEY,IV,userDetails.getPhone()));

            return userDetails;
        }
        catch(Exception e){
            log.error("");
            throw new RuntimeException(e);
        }
    }
}
