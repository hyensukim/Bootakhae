package com.bootakhae.userservice.controllers;

import com.bootakhae.userservice.dto.TokenDto;
import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.global.security.TokenProvider;
import com.bootakhae.userservice.global.mapper.UserMapper;
import com.bootakhae.userservice.services.TokenService;
import com.bootakhae.userservice.services.UserService;
import com.bootakhae.userservice.vo.request.RequestPassword;
import com.bootakhae.userservice.vo.request.RequestUser;
import com.bootakhae.userservice.vo.response.ResponseUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    /**
     * 헬스 체크 
     */
    @GetMapping("health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("service is available");
    }

    /**
     * 로그아웃
     */
    @GetMapping("logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh-token") String refreshToken){
        tokenService.removeRefreshToken(refreshToken);
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("refresh-token".equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                    break;
                }
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 토큰 검증
     */
    @GetMapping("token")
    public ResponseEntity<String> checkToken(){
        return ResponseEntity.status(HttpStatus.OK).body("access-token 검증 : 인증 성공");
    }


    /**
     * 토큰 재발급
     */
    @GetMapping("reissue")
    public ResponseEntity<String> reIssueToken(@CookieValue(name = "refresh-token")String refreshToken){
        TokenDto tokenDetails = tokenService.findTokenByRefreshToken(refreshToken);
        UserDto userDetails = userService.getOneByUserId(tokenDetails.getUserId());
//        String accessToken = tokenProvider.createAccessToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body("토큰 재발급 완료");
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
