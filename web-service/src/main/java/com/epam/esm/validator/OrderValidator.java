package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

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

        if (order.getId() != null) {
            errors.rejectValue("id", "you can not set id for new order");
        }
        if (order.getPrice() != null) {
            errors.rejectValue("price", "you can not set price for order");
        }

        if (order.getCreateDate() != null) {
            errors.rejectValue("createDate", "you can not create/update createDate field");
        }
        if (order.getOperation() != null) {
            errors.rejectValue("operation", "unrecognized field");
        }

        if (order.getTimestamp() != null) {
            errors.rejectValue("timestamp", "unrecognized field");
        }
        checkUser(order.getUser(), errors);
        checkListCertificate(order.getCertificates(), errors);
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
            if (user.getDateOfBirth() != null) {
                errors.rejectValue("user.dateOfBirth", MUST_BE_EMPTY);
            }
            if (user.getEmail() != null) {
                errors.rejectValue("user.email", MUST_BE_EMPTY);
            }
        }
    }

    private void checkListCertificate(List<GiftCertificate> certificates, Errors errors) {
        if (certificates == null) {
            errors.rejectValue("certificates", "empty field");
        } else {
            certificates.forEach(certificate -> checkCertificate(certificate, errors));
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
            if (certificate.getActive() != null) {
                errors.rejectValue("certificate.isActive", MUST_BE_EMPTY);
            }
            if (certificate.getOperation() != null) {
                errors.rejectValue("certificate.operation", "unrecognized field");
            }
            if (certificate.getTimestamp() != null) {
                errors.rejectValue("certificate.timestamp", "unrecognized field");
            }
        }
    }

}
