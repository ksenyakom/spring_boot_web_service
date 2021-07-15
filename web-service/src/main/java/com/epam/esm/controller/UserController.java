package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.UserDto;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.User;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.mapper.UserMapper;
import com.epam.esm.validator.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * Controller class for User
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private UserFacade userFacade;
    private UserDtoValidator userDtoValidator;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserFacade userFacade, UserDtoValidator userDtoValidator, UserMapper userMapper) {
        this.userFacade = userFacade;
        this.userDtoValidator = userDtoValidator;
        this.userMapper = userMapper;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('users:read')")
    public JsonResult<User> getAllUsers(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                        @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                        @RequestParam(value = "includeMetadata", defaultValue = "true") boolean includeMetadata)
            throws ServiceException {
        return userFacade.getAllUsers(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public JsonResult<User> getUser(@PathVariable("id") @Min(1) Integer id) throws ServiceException {
        return userFacade.getUser(id);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('users:read profile')")
    public JsonResult<User> getCurrentUserProfile() throws ServiceException {
        String email = getCurrentUserEmail();
        JsonResult<User> result = userFacade.getUserByEmail(email);
        return result;
    }

//    @PostMapping()
//    public JsonResult<User> userRegistration(@RequestBody UserDto userDto,BindingResult result) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            throw new ServiceException("You are already logged in", "40992");
//        }
//        userDtoValidator.validate(userDto,result);
//        if (result.hasErrors()) {
//            throw new ServiceException(message(result), "42283");
//        }
//        User user = userMapper.mapUserDtoToUser(userDto);
//        JsonResult<User> registered = userFacade.registerNewUserAccount(user);
//        return registered;
//    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getCode()).append("."));
        return sb.toString();
    }

    private String getCurrentUserEmail() {
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userPrincipal.getEmail();
    }
}
