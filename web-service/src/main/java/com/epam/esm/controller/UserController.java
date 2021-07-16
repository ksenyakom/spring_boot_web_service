package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.User;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
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

        return userFacade.getUserByEmail(email);
    }

    private String getCurrentUserEmail() {
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userPrincipal.getEmail();
    }
}
