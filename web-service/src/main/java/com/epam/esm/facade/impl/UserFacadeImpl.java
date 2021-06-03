package com.epam.esm.facade.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.PageMetadata;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public JsonResult<User> getAllUsers(int page, int size, boolean includeMetadata) {
        List<User> users = userService.findAll(page, size);
        PageMetadata pageMetadata = fillMetadata(page, size, includeMetadata);

        return new JsonResult.Builder<User>()
                .withSuccess(true)
                .withResult(users)
                .withMetadata(pageMetadata)
                .build();
    }

    private void addHateoasLinks(User user) {
        user.add(linkTo(methodOn(UserController.class).show(user.getId())).withSelfRel());
    }

    private PageMetadata fillMetadata (int page, int perPage, boolean includeMetadata) {
        if (includeMetadata) {
            int totalFound = userService.countAll();
            PageMetadata pageMetadata = new PageMetadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = pageMetadata.getPageCount();
            pageMetadata.add(linkTo(methodOn(UserController.class).index(page, perPage, includeMetadata)).withSelfRel());
            pageMetadata.add(linkTo(methodOn(UserController.class).index(1, perPage, includeMetadata)).withRel("first"));
            pageMetadata.add(linkTo(methodOn(UserController.class).index(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            pageMetadata.add(linkTo(methodOn(UserController.class).index(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            pageMetadata.add(linkTo(methodOn(UserController.class).index(pageCount, perPage, includeMetadata)).withRel("last"));
            return pageMetadata;
        }
        return null;
    }
}
