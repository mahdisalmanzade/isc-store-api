package com.isc.store.mappers;

import com.isc.store.dtos.RegisterUserRequest;
import com.isc.store.dtos.UpdateUserRequest;
import com.isc.store.dtos.UserDto;
import com.isc.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface UserMapper {
    UserDto mapToUserDto(User user);
    User mapUserDtoToEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
