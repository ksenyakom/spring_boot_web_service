package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserFacadeImplTest {
    @Mock
    UserService userService;


    UserFacade userFacade;

    @BeforeEach
    void setUp() {
        userFacade = new UserFacadeImpl(userService);
    }

    @Test
    void getUser() {
        given(userService.findById(anyInt())).willReturn(new User());
        JsonResult<User> jsonResult = userFacade.getUser(anyInt());

        assertAll(() -> {
            assertEquals(1, jsonResult.getResult().size());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void getAllUsers() {
        given(userService.findAll(1, 5)).willReturn(new ArrayList<>());
        JsonResult<User> jsonResult = userFacade.getAllUsers(1, 5, true);

        assertAll(() -> {
            assertNotNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }
}