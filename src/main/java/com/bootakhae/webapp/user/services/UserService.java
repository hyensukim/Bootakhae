package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    UserDto signup(UserDto dto);

    UserDto getOneByUserId(String userId);

    UserDto getUserDetailsByEmail(String email);

//    AuthInfoDto login(UserDto userDetails);
}
