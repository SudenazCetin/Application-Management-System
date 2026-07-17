package com.company.application.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.application.dto.UserDto;
import com.company.application.entity.User;

// MapStruct annotation processing devre disi olsa bile mapper bean'ini saglar.
@Component
@Primary
public class UserManualMapper implements UserMapper {

    @Override
    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        return dto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User entity = new User();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        return entity;
    }

    @Override
    public List<UserDto> toDtoList(List<User> entities) {
        if (entities == null) {
            return null;
        }

        List<UserDto> result = new ArrayList<>(entities.size());
        for (User entity : entities) {
            result.add(toDto(entity));
        }
        return result;
    }

    @Override
    public List<User> toEntityList(List<UserDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<User> result = new ArrayList<>(dtos.size());
        for (UserDto dto : dtos) {
            result.add(toEntity(dto));
        }
        return result;
    }
}
