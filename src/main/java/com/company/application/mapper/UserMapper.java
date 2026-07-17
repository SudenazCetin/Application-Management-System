package com.company.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.application.dto.UserDto;
import com.company.application.entity.User;

// Bu arayuz, User ile UserDto arasinda donusum saglar.
@Mapper
public interface UserMapper {

    UserDto toDto(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "applicationForms", ignore = true)
    User toEntity(UserDto dto);

    List<UserDto> toDtoList(List<User> entities);

    List<User> toEntityList(List<UserDto> dtos);
}
