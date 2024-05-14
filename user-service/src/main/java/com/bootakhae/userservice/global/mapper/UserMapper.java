package com.bootakhae.userservice.global.mapper;

import com.bootakhae.userservice.dto.UserDto;
import com.bootakhae.userservice.entities.UserEntity;
import com.bootakhae.userservice.vo.request.RequestUser;
import com.bootakhae.userservice.vo.response.ResponseUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // target : entity, source : dto - db 입력 데이터
    @Mapping(target = "email" ,source = "email" )
    @Mapping(target = "password" ,source = "password" )
    @Mapping(target = "address1" ,source = "address1")
    @Mapping(target = "address2" ,source = "address2")
    @Mapping(target = "name" ,source = "name" )
    @Mapping(target = "nickname" ,source = "nickname" )
    @Mapping(target = "phone" ,source = "phone" )
    UserEntity dtoToEntity(UserDto userDto);

    // target : dto, source : entity - db 데이터 반환
    @Mapping(target = "email" ,source = "email" )
    @Mapping(target = "password" ,source = "password" )
    @Mapping(target = "address1" ,source = "address1")
    @Mapping(target = "address2" ,source = "address2")
    @Mapping(target = "name" ,source = "name" )
    @Mapping(target = "nickname" ,source = "nickname" )
    @Mapping(target = "phone" ,source = "phone" )
    @Mapping(target = "userId" ,source = "userId" )
    @Mapping(target = "role" ,source = "role")
    @Mapping(target = "createdAt", source = "createdAt")
    UserDto entityToDto(UserEntity userEntity);

    // target : dto, source : vo - 입력 데이터
    @Mapping(target = "email" ,source = "email" )
    @Mapping(target = "password" ,source = "password" )
    @Mapping(target = "address1" ,source = "address1")
    @Mapping(target = "address2" ,source = "address2")
    @Mapping(target = "name" ,source = "name" )
    @Mapping(target = "nickname" ,source = "nickname" )
    @Mapping(target = "phone" ,source = "phone" )
    @Mapping(target = "userId" ,source = "userId" )
    @Mapping(target = "createdAt", source = "createdAt")
    UserDto voToDto(RequestUser requestUser);

    // target : vo, source : dto - 출력 데이터
    @Mapping(target = "email" ,source = "email" )
    @Mapping(target = "password" ,source = "password" )
    @Mapping(target = "address1" ,source = "address1" )
    @Mapping(target = "address2" ,source = "address2" )
    @Mapping(target = "name" ,source = "name" )
    @Mapping(target = "nickname" ,source = "nickname" )
    @Mapping(target = "phone" ,source = "phone" )
    @Mapping(target = "userId" ,source = "userId" )
    @Mapping(target = "createdAt", source = "createdAt")
    ResponseUser dtoToVo(UserDto userDto);
}
