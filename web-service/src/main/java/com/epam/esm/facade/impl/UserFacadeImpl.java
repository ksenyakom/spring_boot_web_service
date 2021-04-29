package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;

    @Autowired
    public UserFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public JsonResult<User> getUser(int id) {
        User user = userService.findById(id);
        return new JsonResult.Builder<User>()
                .withSuccess(true)
                .withResult(Collections.singletonList(user))
                .build();
    }

    @Override
    public JsonResult<User> getAllUsers() {
        List<User> users = userService.findAll();
        String message = String.format("Found results: %s.", users.size());
        return new JsonResult.Builder<User>()
                .withSuccess(true)
                .withResult(users)
                .withMessage(message)
                .build();
    }
}