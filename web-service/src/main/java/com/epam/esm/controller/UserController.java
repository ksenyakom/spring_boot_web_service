package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for User
 * */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserFacade userFacade;

    @GetMapping()
    public JsonResult<User> index(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "perPage", defaultValue = "5") int perPage,
                                  @RequestParam(value = "includeMetadata", required = false, defaultValue = "false") boolean includeMetadata)
            throws ServiceException {
        return userFacade.getAllUsers(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<User> show(@PathVariable("id") int id) throws ServiceException {
        return userFacade.getUser(id);
    }
}
