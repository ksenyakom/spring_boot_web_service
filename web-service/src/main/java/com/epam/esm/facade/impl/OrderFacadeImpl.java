package com.epam.esm.facade.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dao.DaoException;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.Metadata;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.search.SearchOrderService;
import com.epam.esm.service.search.impl.SearchOrderByUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.cert.Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;

    @Autowired
    public OrderFacadeImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public JsonResult<Order> getOrder(int id) {
        Order order = orderService.findById(id);
        Metadata metadata = new Metadata();
        metadata.add(linkTo(methodOn(OrderController.class).show(id)).withSelfRel());
        metadata.add(linkTo(methodOn(OrderController.class).delete(id)).withRel("delete"));
        metadata.add(linkTo(methodOn(GiftCertificateController.class).show(order.getCertificate().getId())).withRel("certificate"));
        metadata.add(linkTo(methodOn(UserController.class).show(order.getUser().getId())).withRel("user"));

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .withMetadata(metadata)
                .build();
    }

    @Override
    public JsonResult<Order> getOrder(int id, Set<String> fieldsToFind) {
        Order order = orderService.findById(id, fieldsToFind);
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .build();
    }


    @Override
    public JsonResult<Order> save(Order order) {
        orderService.save(order);
        order = orderService.findById(order.getId());
        Metadata metadata = new Metadata();
        metadata.add(linkTo(methodOn(OrderController.class).show(order.getId())).withRel("order"));
        metadata.add(linkTo(methodOn(GiftCertificateController.class).show(order.getCertificate().getId())).withRel("certificate"));

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(Collections.singletonList(order))
                .withMetadata(metadata)
                .build();
    }

    @Override
    public JsonResult<Order> delete(int id) {
        orderService.delete(id);
        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .build();
    }

    @Override
    public JsonResult<Order> getAllOrders(int page, int perPage, boolean includeMetadata) {
        List<Order> orders = orderService.findAll(page, perPage);
        int totalFound = orderService.countAll();
        Metadata metadata = fillMetadata(includeMetadata, page, perPage, orders, totalFound);

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(orders)
                .withMetadata(metadata)
                .build();
    }

    @Override
    public JsonResult<Order> search(int userId, int page, int perPage, boolean includeMetadata) {
        SearchOrderService searchOrderService = new SearchOrderByUser(new User(userId), page, perPage);
        List<Order> orders = searchOrderService.search(orderService);
        int totalFound = searchOrderService.getTotalFound();
        Metadata metadata = fillMetadata(includeMetadata, page, perPage, orders, totalFound);

        return new JsonResult.Builder<Order>()
                .withSuccess(true)
                .withResult(orders)
                .withMetadata(metadata)
                .build();
    }

    private Metadata fillMetadata(boolean includeMetadata, int page, int perPage, List<Order> orders, int totalFound) {
        if (includeMetadata) {
            Metadata metadata = new Metadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0 ? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = metadata.getPageCount();
            metadata.add(linkTo(methodOn(OrderController.class).index(page, perPage, includeMetadata)).withSelfRel());
            metadata.add(linkTo(methodOn(OrderController.class).index(1, perPage, includeMetadata)).withRel("first"));
            metadata.add(linkTo(methodOn(OrderController.class).index(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            metadata.add(linkTo(methodOn(OrderController.class).index(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            metadata.add(linkTo(methodOn(OrderController.class).index(pageCount, perPage, includeMetadata)).withRel("last"));
            for (Order order : orders) {
                metadata.add(linkTo(methodOn(OrderController.class).show(order.getId())).withRel("order"));
                metadata.add(linkTo(methodOn(GiftCertificateController.class).show(order.getCertificate().getId())).withRel("certificate"));
            }

            return metadata;
        }
        return null;
    }
}
