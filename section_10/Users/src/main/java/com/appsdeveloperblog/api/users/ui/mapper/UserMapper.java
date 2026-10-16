package com.appsdeveloperblog.api.users.ui.mapper;

import com.appsdeveloperblog.api.users.io.UserEntity;
import com.appsdeveloperblog.api.users.shared.UserDto;
import com.appsdeveloperblog.api.users.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.api.users.ui.response.UserRest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(UserDetailsRequestModel userDetails);
    UserRest toUserRest(UserDto userDto);
    List<UserRest> toUserRestList(List<UserDto> users);
    UserEntity toUserEntity(UserDto userDto);
    UserDto toUserDtoFromUserEntity(UserEntity userEntity);
    List<UserDto> toUserDtoList(List<UserEntity> users);
}
