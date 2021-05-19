package com.epam.esm.validator;

import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;

class TagInCertificateValidator {
    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 2;

    public void validateTag(@NonNull Tag tag, Errors errors) {
        if (tag.getId() == null && tag.getName() == null) {
            errors.rejectValue("tags", "empty tag, name and id of tag is empty");
        }

        if (tag.getName() != null) {
            if (tag.getName().length() < MIN_LENGTH || tag.getName().length() > MAX_LENGTH) {
                errors.rejectValue("tag.name", "invalid length");
            }
        }
        if (tag.getOperation() != null) {
            errors.rejectValue("tag.operation", "must be null");
        }

        if (tag.getTimestamp() != null) {
            errors.rejectValue("tag.timestamp", "must be null");
        }
    }
}
