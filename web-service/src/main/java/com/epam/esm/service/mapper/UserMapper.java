package com.epam.esm.service.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;

public interface UserMapper {
    User mapUserDtoToUser(UserDto userDto);
}