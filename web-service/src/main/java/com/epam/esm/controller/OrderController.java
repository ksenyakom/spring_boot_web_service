package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.Order;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping()
    public JsonResult<Order> index() {
        return orderFacade.getAllOrders();
    }

    @GetMapping("/{id}")
    public JsonResult<Order> show(@PathVariable("id") int id) {
        return orderFacade.getOrder(id);
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