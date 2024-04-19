package com.bootakhae.webapp.user.controllers;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.mapper.UserMapper;
import com.bootakhae.webapp.user.services.UserService;

import com.bootakhae.webapp.user.vo.request.RequestPassword;
import com.bootakhae.webapp.user.vo.request.RequestUser;
import com.bootakhae.webapp.user.vo.response.ResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 헬스 체크 
     */
    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("service is available");
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
                                                           @RequestBody RequestPassword request){
        UserDto userDetails = userService.updateUserPassword(userId,
                request.getPassword(),
                request.getNewPassword(),
                request.getConfirmPassword());
        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

//    todo: 참고! - 해당 기능은 Controller 없이 UsernamePasswordAuthenticationFilter 상속받아서 구현함
//    /**
//     * 로그인
//     */
//    @PostMapping("login")
//    public ResponseEntity<ResponseUser> login(@Valid @RequestBody RequestLogin request){
//        AuthInfoDto authInfoDto = userService.login(UserMapper.INSTANCE.voToDto(request));
//        ResponseUser responseUser = UserMapper.INSTANCE.dtoToVo(authInfoDto.getUserDetails());
//        return ResponseEntity.status(HttpStatus.OK)
//                .header("token", authInfoDto.getAccessToken())
//                .body(responseUser);
//    }
}
