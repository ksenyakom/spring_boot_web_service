package com.epam.esm.validator;

import java.util.Set;

public interface FieldNameValidator {
    Set<String> validate(String fields);
}
