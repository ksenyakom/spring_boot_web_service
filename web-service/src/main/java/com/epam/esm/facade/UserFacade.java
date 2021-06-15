package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;

/**
 * Defines methods for facade layout for User class.
 * Methods supposed to wrap results in JsonResult class.
 */
public interface UserFacade {
    JsonResult<User> getUser(int id);

    JsonResult<User> getAllUsers(int page, int size, boolean includeMetadata);

    JsonResult<User> registerNewUserAccount(User user);

    JsonResult<User> getUserByEmail(String email);
}
