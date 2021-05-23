package com.epam.esm.facade.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.Metadata;
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
    public JsonResult<User> getUser(int id, boolean includeMetadata) {
        User user = userService.findById(id);
        Metadata metadata = new Metadata();
        metadata.add(linkTo(methodOn(UserController.class).show(id, includeMetadata)).withSelfRel());
        metadata.add(linkTo(methodOn(OrderController.class).search(id,null, null, includeMetadata)).withRel("orders"));

        return new JsonResult.Builder<User>()
                .withSuccess(true)
                .withResult(Collections.singletonList(user))
                .withMetadata(metadata)
                .build();
    }

    @Override
    public JsonResult<User> getAllUsers(int page, int size, boolean includeMetadata) {
        List<User> users = userService.findAll(page, size);
        Metadata metadata = fillMetadata(page, size, includeMetadata);

        return new JsonResult.Builder<User>()
                .withSuccess(true)
                .withResult(users)
                .withMetadata(metadata)
                .build();
    }

    private Metadata fillMetadata (int page, int perPage, boolean includeMetadata) {
        if (includeMetadata) {
            int totalFound = userService.countAll();
            Metadata metadata = new Metadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = metadata.getPageCount();
            metadata.add(linkTo(methodOn(UserController.class).index(page, perPage, includeMetadata)).withSelfRel());
            metadata.add(linkTo(methodOn(UserController.class).index(1, perPage, includeMetadata)).withRel("first"));
            metadata.add(linkTo(methodOn(UserController.class).index(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            metadata.add(linkTo(methodOn(UserController.class).index(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            metadata.add(linkTo(methodOn(UserController.class).index(pageCount, perPage, includeMetadata)).withRel("last"));
            return metadata;
        }
        return null;
    }
}
