package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.Order;
import com.epam.esm.service.ServiceException;
import com.epam.esm.validator.FieldNameValidator;
import com.epam.esm.validator.OrderFieldNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Controller class for Order
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    @Qualifier("orderValidator")
    private Validator orderValidator;

    @Autowired
    @Qualifier("orderFieldNameValidator")
    private FieldNameValidator fieldNameValidator;

    @GetMapping()
    public JsonResult<Order> index() {
        return orderFacade.getAllOrders();
    }

    @GetMapping("/{id}")
    public JsonResult<Order> show(@PathVariable("id") int id) {
        JsonResult<Order> jsonResult =  orderFacade.getOrder(id);
        jsonResult.add(linkTo(methodOn(OrderController.class).show(id)).withSelfRel());
        jsonResult.add(linkTo(methodOn(OrderController.class).delete(id)).withRel("delete"));
        return jsonResult;
    }

    @GetMapping(value = "/{id}", params = {"fields"})
    public JsonResult<Order> showFields(@PathVariable("id") int id, @RequestParam("fields") String fields) {
        Set<String> fieldsToFind = fieldNameValidator.validate(fields);
        return orderFacade.getOrder(id, fieldsToFind);
    }

    @GetMapping("/search")
    public JsonResult<Order> search(@RequestParam("userId") int userId) {
        JsonResult<Order> result = orderFacade.search(userId);
        for (Order order: result.getResult()) {
            result.add(linkTo(methodOn(OrderController.class).show(order.getId())).withRel("order"));
            result.add(linkTo(methodOn(GiftCertificateController.class).show(order.getCertificate().getId())).withRel("certificate"));
        }
        return result;
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<Order> create(@RequestBody Order order, BindingResult result) {
        orderValidator.validate(order, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return orderFacade.save(order);
    }

    @PutMapping("/{id}")
    public JsonResult<Order> update(@RequestBody Order order, BindingResult result,
                                              @PathVariable("id") int id) {
        order.setId(id);
        orderValidator.validate(order, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return orderFacade.save(order);
    }

    @DeleteMapping("/{id}")
    public JsonResult<Order> delete(@PathVariable("id") int id) {

        return orderFacade.delete(id);
    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getCode()).append("."));

        return sb.toString();
    }

}