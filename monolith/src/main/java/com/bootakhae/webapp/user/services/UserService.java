package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    UserDto signup(UserDto dto);

    UserDto getOneByUserId(String userId);

    UserDto getUserDetailsByEmail(String email);

    UserDto updateUserInfo(UserDto userDetails, String userId);

    UserDto updateUserPassword(String userId, String oldPw, String newPw, String confirmPw);

//    AuthInfoDto login(UserDto userDetails);
}
