package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.OrderMapper;
import com.epam.esm.validator.FieldNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    @Qualifier("orderValidator")
    private Validator orderValidator;

    @Autowired
    @Qualifier("orderFieldNameValidator")
    private FieldNameValidator fieldNameValidator;

    /**
     * Finds all orders:
     * for admin - all existing orders for all users which have isActive = true;
     * for user - all user orders which have isActive = true.
     *
     * @param page            - page number to show.
     * @param perPage         - number of orders to show on page.
     * @param includeMetadata - if true information about pagination will be added
     *                        to result object (total pages, total found, HATEOAS
     *                        links for first, previous, next, last pages).
     * @return jsonResult with found orders.
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('orders:read')")
    public JsonResult<Order> getAll(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                    @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                    @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) {
        User user = getCurrentUser();
        return user.getRole() == Role.ADMIN
                ? orderFacade.getAllOrders(page, perPage, includeMetadata)
                : orderFacade.search(user.getId(), page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:read')")
    public JsonResult<Order> getCertificate(@PathVariable("id") @Min(1) Integer id) {
        JsonResult<Order> result = orderFacade.getOrder(id);
        checkUserAuthorizedForGettingResource(result.getResult().get(0));
        return result;
    }

    @GetMapping(value = "/{id}", params = {"fields"})
    @PreAuthorize("hasAuthority('orders:read')")
    public JsonResult<Order> getFields(@PathVariable("id") @Min(1) Integer id, @RequestParam("fields") String fields) {
        Set<String> fieldsToFind = fieldNameValidator.validate(fields);

        JsonResult<Order> result = orderFacade.getOrder(id);

        checkUserAuthorizedForGettingResource(result.getResult().get(0));
        Order order = orderMapper.copyDefinedFields(result.getResult().get(0), fieldsToFind);
        result.setResult(Collections.singletonList(order));
        return result;
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('orders:read by user')")
    public JsonResult<Order> search(@RequestParam("userId") @Min(1) Integer userId,
                                    @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                    @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                    @RequestParam(value = "includeMetadata", defaultValue = "true") boolean includeMetadata) {
        return orderFacade.search(userId, page, perPage, includeMetadata);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('orders:create')")
    public JsonResult<Order> create(@RequestBody Order order, BindingResult result) {
        orderValidator.validate(order, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return orderFacade.save(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:update')")
    public JsonResult<Order> update(@RequestBody Order order, BindingResult result,
                                    @PathVariable("id") @Min(1) Integer id) {

        orderValidator.validate(order, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }
        order.setId(id);

        return orderFacade.save(order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:delete')")
    public JsonResult<Order> delete(@PathVariable("id") @Min(1) Integer id) {

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

    private User getCurrentUser() {
        return userService.findByEmail(getCurrentUserEmail());
    }

    private String getCurrentUserEmail() {
        return ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
    }

    private void checkUserAuthorizedForGettingResource(Order order){
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.USER) {
            if (!order.getUser().getId().equals(currentUser.getId())) {
                throw new ServiceException("Access denied", "40391");
            }
        }
    }

}