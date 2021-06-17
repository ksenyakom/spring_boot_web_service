package com.epam.esm.service.mapper.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;
import com.epam.esm.service.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private ModelMapper mapper;

    @Override
    public User mapUserDtoToUser(UserDto userDto) {
        return Objects.isNull(userDto) ? null : mapper.map(userDto, User.class);

    }
}
