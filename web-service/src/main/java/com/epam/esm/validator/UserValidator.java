package com.epam.esm.validator;

import com.epam.esm.model.User;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class UserValidator implements Validator {
    private static final String REGEX_NAME = "[A-Za-zА-Яа-я0-9_\\-]{2,255}";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_.+\\-]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-.]+";

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        User user = (User) o;

        if (user.getId() != null && user.getId() < 0) {
            errors.rejectValue("id", "id must be equal or grater then 0");
        }

        if (user.getName() == null) {
            errors.rejectValue("name", "empty field");
        } else if (user.getName().matches(REGEX_NAME)) {
            errors.rejectValue("name", "name can contain only letters and _ or -, length 2-255 characters");
        }


        if (user.getSurname() == null) {
            errors.rejectValue("surname", "empty field");
        } else if (user.getSurname().matches(REGEX_NAME)) {
            errors.rejectValue("surname", "surname can contain only letters and _ or -, length 2-255 characters");
        }

        if (user.getDateOfBirth() == null) {
            errors.rejectValue("age", "empty field");
        }

        if (user.getEmail() == null) {
            errors.rejectValue("email", "empty field");
        } else if (!user.getEmail().matches(REGEX_EMAIL)) {
            errors.rejectValue("email", "invalid value");
        }

    }

}
