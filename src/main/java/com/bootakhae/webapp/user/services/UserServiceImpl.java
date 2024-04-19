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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    // todo: 실제 서비스 시에는 해당값을 고정값으로 지정
    private static final SecretKey SECRETKEY = AesCryptoUtil.getKey();
    private static final IvParameterSpec IV = AesCryptoUtil.getIv();

    private final UserRepository userRepository;
    private final Environment env;

    @Transactional
    @Override
    public UserDto signup(UserDto userDetails) {
        log.debug("회원 가입 서비스 실행");
        CryptoInfoDto cryptoInfo = userInfoEncrypt(userDetails);
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(cryptoInfo.getUserDetails());
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(userEncrypt(userDetails));

        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("회원 가입 실패 : 중복된 이메일 입니다.");
        }

        user.changePw(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        return userInfoDecrypt(userDto, cryptoInfo.getKey(), cryptoInfo.getIv());
    @Override
    public UserDto getUserDetailsByEmail(String encryptedEmail) {
        log.debug("JWT 발급 : 이메일로 회원 정보 조회 실행");
        UserEntity user = userRepository.findByEmail(encryptedEmail)
                .orElseThrow(()-> new RuntimeException("JWT 발급 : 가입되지 않은 회원입니다."));
        UserDto userDto = UserMapper.INSTANCE.entityToDto(user);
        return userDecrypt(userDto);
    }
    }

    private CryptoInfoDto userInfoEncrypt(UserDto userDetails){
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String EncryptedEmail = userInfoEncrypt(username);

        UserEntity userEntity = userRepository.findByEmail(EncryptedEmail)
                .orElseThrow(()->new UsernameNotFoundException(username));

        String authorities = userEntity.getRole().name();

        List<SimpleGrantedAuthority> authorityList = Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new).toList();

        return new User(userEntity.getEmail(), userEntity.getPassword(),
                true,true,true,true,
                authorityList);
    }

//    @Override
//    public AuthInfoDto login(UserDto userDetails) {
//        log.debug("회원 로그인 실행");
//        String encryptedEmail = userInfoEncrypt(userDetails.getEmail());
//
//        UserEntity user = userRepository.findByEmail(encryptedEmail).orElseThrow(
//            () -> new RuntimeException("로그인 실패 : 가입하지 않은 회원입니다.")
//        );
//
//        if(!passwordEncoder.matches(userDetails.getPassword(), user.getPassword())){
//            throw new RuntimeException("로그인 실패 : 아이디와 비밀번호가 일치하지 않습니다.");
//        }
//        /* todo : UserDetails를 커스텀 해줘야 이렇게 짤 수 있는건가? */
////        Authentication authentication = null;
////        try {
////             authentication = authenticationManagerBuilder.getObject()
////                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
////        }catch(Exception e){
////            throw new RuntimeException(e);
////        }
//        userDetails = UserMapper.INSTANCE.entityToDto(user);
//
//        String accessToken = tokenProvider.createToken(user.getUserId());
//
//        return AuthInfoDto.builder()
//                .userDetails(userDecrypt(userDetails))
//                .accessToken(accessToken)
//                .build();
//    }

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
