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
    private int maxLength = 255;
    private int minLength = 2;

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
        } else if (certificate.getName().length() < minLength || certificate.getName().length() > maxLength) {
            errors.rejectValue("name", "invalid length");
        }

        String description = certificate.getDescription();
        if (description == null) {
            errors.rejectValue("description", "empty field");
        } else if (description.isEmpty() || description.length() > maxLength) {
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
            for (Tag tag : certificate.getTags()) {
                validateTag(tag, errors);
            }
        }

    }

    private void validateTag(@NonNull Tag tag, Errors errors) {
        if (tag.getId() == null && tag.getName() == null) {
            errors.rejectValue("tags", "empty tag, name and id of tag is empty");
        }

        if (tag.getName() != null) {
            if (tag.getName().length() < minLength || tag.getName().length() > maxLength) {
                errors.rejectValue("tag.name", "invalid length");
            }
        }
    }
}
