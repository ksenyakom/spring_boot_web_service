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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Controller class for Order
 */
@RestController
@RequestMapping("/orders")
@Validated
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
    public JsonResult<Order> index(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                   @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                   @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) {
        return orderFacade.getAllOrders(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<Order> show(@PathVariable("id") @Min(1) Integer id) {
        return orderFacade.getOrder(id);
    }

    @GetMapping(value = "/{id}", params = {"fields"})
    public JsonResult<Order> showFields(@PathVariable("id") @Min(1) Integer id, @RequestParam("fields") String fields) {
        Set<String> fieldsToFind = fieldNameValidator.validate(fields);
        return orderFacade.getOrder(id, fieldsToFind);
    }

    @GetMapping("/search")
    public JsonResult<Order> search(@RequestParam("userId") @Min(1) Integer userId,
                                    @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                    @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                    @RequestParam(value = "includeMetadata", defaultValue = "true") boolean includeMetadata) {
        return orderFacade.search(userId, page, perPage, includeMetadata);
    }


}