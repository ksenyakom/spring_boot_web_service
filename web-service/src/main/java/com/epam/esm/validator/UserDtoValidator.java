package com.epam.esm.validator;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class UserDtoValidator implements Validator {
    private static final String REGEX_NAME = "[A-Za-zА-Яа-я0-9_\\-]{2,255}";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_.+\\-]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-.]+";
    private static final String REGEX_PASSWORD = ".{4,255}"; //TODO make password stronger

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        UserDto user = (UserDto) o;


        if (user.getName() == null) {
            errors.rejectValue("name", "empty field");
        } else if (!user.getName().matches(REGEX_NAME)) {
            errors.rejectValue("name", "name can contain only letters and _ or -, length 2-255 characters");
        }


        if (user.getSurname() == null) {
            errors.rejectValue("surname", "empty field");
        } else if (!user.getSurname().matches(REGEX_NAME)) {
            errors.rejectValue("surname", "surname can contain only letters and _ or -, length 2-255 characters");
        }

        if (user.getEmail() == null) {
            errors.rejectValue("email", "empty field");
        } else if (!user.getEmail().matches(REGEX_EMAIL)) {
            errors.rejectValue("email", "invalid value");
        }

        if (user.getPassword() == null) {
            errors.rejectValue("password", "empty field");
        } else if (!user.getPassword().matches(REGEX_PASSWORD)) {
            errors.rejectValue("password", "length 4-255 characters");
        } else if (!Objects.equals(user.getPassword(), user.getMatchingPassword())) {
            errors.rejectValue("password", "passwords not equal");
            errors.rejectValue("matchingPassword", "passwords not equal");
        }

    }

}
