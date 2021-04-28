package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Validator for patch update of GiftCertificate.
 * Validates only fields which are not null.
 */

@Service
public class GiftCertificatePartValidator implements Validator {
    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 2;

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return GiftCertificate.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        GiftCertificate certificate = (GiftCertificate) o;

        if (certificate.getId() == null) {
            errors.rejectValue("id", "empty field");
        } else if (certificate.getId() < 0) {
            errors.rejectValue("id", "id must be equal or grater then 0");
        }

        if (certificate.getName() != null) {
            if (certificate.getName().length() < MIN_LENGTH || certificate.getName().length() > MAX_LENGTH) {
                errors.rejectValue("name", "invalid length");
            }
        }

        String description = certificate.getDescription();
        if (description != null) {
            if (description.isEmpty() || description.length() > MAX_LENGTH) {
                errors.rejectValue("description", "invalid length");
            }
        }

        if (certificate.getDuration() < 0) {
            errors.rejectValue("duration", "invalid value, must be equal or grater then 0");
        }

        if (certificate.getPrice() != null) {
            if (certificate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("price", "invalid value");
            }
        }

        if (certificate.getTags() != null) {
            TagInCertificateValidator tagValidator = new TagInCertificateValidator();
            for (Tag tag : certificate.getTags()) {
                tagValidator.validateTag(tag, errors);
            }
        }
    }
}
