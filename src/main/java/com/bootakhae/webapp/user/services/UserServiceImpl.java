package com.bootakhae.webapp.user.services;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.mapper.UserMapper;
import com.bootakhae.webapp.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDto signup(UserDto dto) {
        UserEntity user  = UserMapper.INSTANCE.dtoToEntity(dto);
        user = userRepository.save(user);
        return UserMapper.INSTANCE.entityToDto(user);
    }
}
