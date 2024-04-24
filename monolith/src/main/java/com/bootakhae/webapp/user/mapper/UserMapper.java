package com.bootakhae.webapp.user.mapper;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.vo.request.RequestLogin;
import com.bootakhae.webapp.user.vo.request.RequestUser;
import com.bootakhae.webapp.user.vo.response.ResponseUser;
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
    @Mapping(target = "resEmail" ,source = "email" )
    @Mapping(target = "resPassword" ,source = "password" )
    @Mapping(target = "resAddress1" ,source = "address1" )
    @Mapping(target = "resAddress2" ,source = "address2" )
    @Mapping(target = "resName" ,source = "name" )
    @Mapping(target = "resNickname" ,source = "nickname" )
    @Mapping(target = "resPhone" ,source = "phone" )
    @Mapping(target = "resUserId" ,source = "userId" )
    @Mapping(target = "resCreatedAt", source = "createdAt")
    ResponseUser dtoToVo(UserDto userDto);
}
