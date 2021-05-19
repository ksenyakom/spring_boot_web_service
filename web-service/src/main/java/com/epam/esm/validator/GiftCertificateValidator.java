package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Validator for GiftCertificate class.
 */
@Service
public class GiftCertificateValidator implements Validator {
    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 2;

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return GiftCertificate.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        GiftCertificate certificate = (GiftCertificate) o;

        if (certificate.getId() != null && certificate.getId() < 0) {
            errors.rejectValue("id", "id must be equal or grater then 0");
        }

        if (certificate.getName() == null) {
            errors.rejectValue("name", "empty field");
        } else if (certificate.getName().length() < MIN_LENGTH || certificate.getName().length() > MAX_LENGTH) {
            errors.rejectValue("name", "invalid length");
        }

        String description = certificate.getDescription();
        if (description == null) {
            errors.rejectValue("description", "empty field");
        } else if (description.isEmpty() || description.length() > MAX_LENGTH) {
            errors.rejectValue("description", "invalid length");
        }

        if (certificate.getDuration() <= 0) {
            errors.rejectValue("duration", "invalid value, must be equal or grater then 0");
        }

        if (certificate.getPrice() == null) {
            errors.rejectValue("price", "empty field");
        } else if (certificate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "invalid value");
        }

        if (certificate.getTags() == null || certificate.getTags().isEmpty()) {
            errors.rejectValue("tags", "empty field");
        } else {
            TagInCertificateValidator tagValidator = new TagInCertificateValidator();
            for (Tag tag : certificate.getTags()) {
                tagValidator.validateTag(tag, errors);
            }
        }

        if (certificate.getOperation() != null) {
            errors.rejectValue("operation", "must be null");
        }

        if (certificate.getTimestamp() != null) {
            errors.rejectValue("timestamp", "must be null");
        }
    }

}
