package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagDtoValidator implements Validator {

    private static final int MAX_LENGTH = 255;
    private static final int MIN_LENGTH = 2;

    @Override
    public boolean supports(Class<?> aClass) {
        return TagDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TagDto tag = (TagDto) o;

        if (tag.getName() == null) {
            errors.rejectValue("name", "empty value");
        } else {
            tag.setName(tag.getName().trim());
            if (tag.getName().length() < MIN_LENGTH || tag.getName().length() > MAX_LENGTH) {
                errors.rejectValue("name", "invalid length");
            }
        }
    }

}
