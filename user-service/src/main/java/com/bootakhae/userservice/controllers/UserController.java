package com.bootakhae.userservice.controllers;

import com.bootakhae.userservice.dto.TokenDto;
import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.global.mapper.UserMapper;
import com.bootakhae.userservice.services.RefreshTokenService;
import com.bootakhae.userservice.services.UserService;
import com.bootakhae.userservice.vo.request.RequestPassword;
import com.bootakhae.userservice.vo.request.RequestUser;
import com.bootakhae.userservice.vo.response.ResponseToken;
import com.bootakhae.userservice.vo.response.ResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final Environment env;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    /**
     * 헬스 체크 
     */
    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck(){
        String body = String.format("port(local.server.port) :" +  env.getProperty("local.server.port")
        +", port(server.port) :" + env.getProperty("server.port")
        +", with token secret :" + env.getProperty("token.secret")
        +", with token access time : " + env.getProperty("token.access-expired-time")
        +", with token refresh time : " + env.getProperty("token.refresh-expired-time"));
        return ResponseEntity.ok(body);
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("{userId}/logout")
    public ResponseEntity<Void> logout(@PathVariable("userId") String userId){
        refreshTokenService.removeTokenInfo(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 토큰 재발급
     */
    @GetMapping("{userId}/reissue")
    public ResponseEntity<ResponseToken> reIssueToken(@PathVariable("userId") String userId){
        TokenDto tokens = refreshTokenService.findAndValidate(userId);
        refreshTokenService.saveTokenInfo(tokens.getUserId(), tokens.getRefreshToken());
        ResponseToken responseToken = tokens.dtoToVo();
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseToken);
    }

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity<ResponseUser> signUp(@Valid @RequestBody RequestUser request){
        UserDto userDetails = UserMapper.INSTANCE.voToDto(request);
        userDetails  = userService.signup(userDetails);
        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    /**
     * 회원 상세 조회
     */
    @GetMapping("{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDetails = userService.getOneByUserId(userId);
        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("{userId}")
    public ResponseEntity<ResponseUser> updateUser(@PathVariable("userId") String userId,
                                                   @RequestBody RequestUser request){
        UserDto userDetails = UserMapper.INSTANCE.voToDto(request);
        userDetails = userService.updateUserInfo(userDetails, userId);
        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("{userId}/password")
    public ResponseEntity<ResponseUser> updateUserPassword(@PathVariable("userId") String userId,
                                                           @Valid @RequestBody RequestPassword request){
        UserDto userDetails = userService.updateUserPassword(userId,
                request.getPassword(),
                request.getNewPassword(),
                request.getConfirmPassword());
        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
}
