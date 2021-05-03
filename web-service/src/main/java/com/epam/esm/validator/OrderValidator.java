package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class OrderValidator implements Validator {

    private static final String MUST_BE_EMPTY = "must be empty field";

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
        if (order.getCreateDate() != null) {
            errors.rejectValue("createDate", "you can not create/update createDate field");
        }
        checkUser(order.getUser(), errors);
        checkCertificate(order.getCertificate(), errors);
    }

    private void checkUser(User user, Errors errors) {
        if (user == null) {
            errors.rejectValue("user", "empty field");
        } else {
            if (user.getId() == null) {
                errors.rejectValue("user.id", "empty field");
            }
            if (user.getName() != null) {
                errors.rejectValue("user.name", MUST_BE_EMPTY);
            }
            if (user.getSurname() != null) {
                errors.rejectValue("user.surname", MUST_BE_EMPTY);
            }
            if (user.getAge() != 0) {
                errors.rejectValue("user.age", MUST_BE_EMPTY);
            }
            if (user.getEmail() != null) {
                errors.rejectValue("user.email", MUST_BE_EMPTY);
            }
        }
    }

    private void checkCertificate(GiftCertificate certificate, Errors errors) {
        if (certificate == null) {
            errors.rejectValue("certificate", "empty field");
        } else {
            if (certificate.getId() == null) {
                errors.rejectValue("certificate.id", "empty field");
            }

            if (certificate.getName() != null) {
                errors.rejectValue("certificate.name", MUST_BE_EMPTY);
            }
            if (certificate.getDescription() != null) {
                errors.rejectValue("certificate.description", MUST_BE_EMPTY);
            }
            if (certificate.getDuration() != 0) {
                errors.rejectValue("certificate.duration", MUST_BE_EMPTY);
            }
            if (certificate.getPrice() != null) {
                errors.rejectValue("certificate.price", MUST_BE_EMPTY);
            }
            if (certificate.getTags() != null) {
                errors.rejectValue("certificate.tags", MUST_BE_EMPTY);
            }
            if (certificate.getLastUpdateDate() != null) {
                errors.rejectValue("certificate.lastUpdateDate", MUST_BE_EMPTY);
            }
            if (certificate.getCreateDate() != null) {
                errors.rejectValue("certificate.createDate", MUST_BE_EMPTY);
            }
            if (certificate.getIsActive()) {
                errors.rejectValue("certificate.isActive", MUST_BE_EMPTY);
            }
        }
    }

}
