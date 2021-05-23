package com.epam.esm.validator;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagValidator implements Validator {

    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 2;

    @Override
    public boolean supports(Class<?> aClass) {
        return Tag.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tag tag = (Tag) o;
        if (tag.getId() != null) {
            errors.rejectValue("id", "you can not set id for new tag");

        }
        if (tag.getName() == null) {
            errors.rejectValue("name", "empty value");
        } else {
            if (tag.getName().length() < MIN_LENGTH || tag.getName().length() > MAX_LENGTH) {
                errors.rejectValue("name", "invalid length");
            }
        }

        if (tag.getOperation() != null) {
            errors.rejectValue("operation", "must be null");
        }

        if (tag.getTimestamp() != null) {
            errors.rejectValue("timestamp", "must be null");
        }
    }

}
