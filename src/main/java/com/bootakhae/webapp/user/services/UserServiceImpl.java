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
            SecretKey key = AesCryptoUtil.getKey();
            IvParameterSpec iv = AesCryptoUtil.getIv();
            String specName = env.getProperty("aes.spec");

            userDetails.setAddress1(AesCryptoUtil.encrypt(specName,key,iv,userDetails.getAddress1()));
            userDetails.setAddress2(AesCryptoUtil.encrypt(specName,key,iv,userDetails.getAddress2()));
            userDetails.setEmail(AesCryptoUtil.encrypt(specName,key,iv,userDetails.getEmail()));
            userDetails.setName(AesCryptoUtil.encrypt(specName,key,iv,userDetails.getName()));
            userDetails.setPhone(AesCryptoUtil.encrypt(specName,key,iv,userDetails.getPhone()));

            return new CryptoInfoDto(userDetails,key, iv);
        }
        catch(Exception e){
            log.error("");
            throw new RuntimeException(e);
        }
    }

    private UserDto userInfoDecrypt(UserDto userDetails, SecretKey key, IvParameterSpec iv){
        try{
            String specName = env.getProperty("aes.spec");

            userDetails.setAddress1(AesCryptoUtil.decrypt(specName,key,iv,userDetails.getAddress1()));
            userDetails.setAddress2(AesCryptoUtil.decrypt(specName,key,iv,userDetails.getAddress2()));
            userDetails.setEmail(AesCryptoUtil.decrypt(specName,key,iv,userDetails.getEmail()));
            userDetails.setName(AesCryptoUtil.decrypt(specName,key,iv,userDetails.getName()));
            userDetails.setPhone(AesCryptoUtil.decrypt(specName,key,iv,userDetails.getPhone()));

            return userDetails;
        }
        catch(Exception e){
            log.error("");
            throw new RuntimeException(e);
        }
    }
}
