package com.epam.esm.validator;

import com.epam.esm.model.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class OrderValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return Order.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        Order order = (Order) o;


        if (order.getId() != null && order.getId() < 0) {
            errors.rejectValue("id", "id must be equal or grater then 0");
        }

        if (order.getPrice() == null) {
            errors.rejectValue("price", "empty field");
        } else if (order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "invalid value");
        }


        if (order.getCertificate() == null) {
            errors.rejectValue("certificate", "empty field");
        } else if (order.getCertificate().getId() == null) {
            errors.rejectValue("certificate.id", "empty field");
        }

        if (order.getUser() == null) {
            errors.rejectValue("user", "empty field");
        } else if (order.getUser().getId() == null) {
            errors.rejectValue("user.id", "empty field");
        }

        if (order.getCreateDate() != null) {
            errors.rejectValue("createDate", "you can not create/update createDate field");
        }

    }

}
