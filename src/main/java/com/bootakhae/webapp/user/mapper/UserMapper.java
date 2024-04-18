package com.bootakhae.webapp.user.mapper;

import com.bootakhae.webapp.user.dto.UserDto;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.user.vo.RequestUser;
import com.bootakhae.webapp.user.vo.ResponseUser;
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
    UserEntity dtoToEntity(UserDto orderDto);

    // target : dto, source : entity - db 데이터 반환
    @Mapping(target = "email" ,source = "email" )
    @Mapping(target = "password" ,source = "password" )
    @Mapping(target = "address1" ,source = "address1")
    @Mapping(target = "address2" ,source = "address2")
    @Mapping(target = "name" ,source = "name" )
    @Mapping(target = "nickname" ,source = "nickname" )
    @Mapping(target = "phone" ,source = "phone" )
    @Mapping(target = "userId" ,source = "userId" )
    @Mapping(target = "createdAt", source = "createdAt")
    UserDto entityToDto(UserEntity orderEntity);

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
    UserDto voToDto(RequestUser requestOrder);

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
//    @Mapping(target = "email" ,source = "resEmail" )
//    @Mapping(target = "password" ,source = "resPassword" )
//    @Mapping(target = "address1" ,source = "resAddress1" )
//    @Mapping(target = "address2" ,source = "resAddress2" )
//    @Mapping(target = "name" ,source = "resName" )
//    @Mapping(target = "nickname" ,source = "resNickname" )
//    @Mapping(target = "phone" ,source = "resPhone" )
//    @Mapping(target = "userId" ,source = "resUserId" )
//    @Mapping(target = "createdAt", source = "resCreatedAt")
    ResponseUser dtoToVo(UserDto orderDto);

    // target : vo, source : entity - 필요 시 정의
//    @Mapping(target = "productId" ,source = "productId" )
//    @Mapping(target = "qty" ,source = "qty" )
//    @Mapping(target = "unitPrice" ,source = "unitPrice" )
//    @Mapping(target = "totalPrice" ,source = "totalPrice" )
//    @Mapping(target = "orderId" ,source = "orderId" )
//    @Mapping(target = "createdAt", source = "createdAt")
//    ResponseUser entityToVo(UserEntity orderEntity);
}
