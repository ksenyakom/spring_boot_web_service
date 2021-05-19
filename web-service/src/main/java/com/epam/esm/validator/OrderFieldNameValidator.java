package com.epam.esm.validator;

import com.epam.esm.model.Order;
import com.epam.esm.service.ServiceException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderFieldNameValidator implements FieldNameValidator {
    @Override
    public Set<String> validate(String line) {

        if (line.contains("operation") || line.contains("timestamp")) {
            throw new ServiceException("Fields timestamp or operation can not be used", "53");
        }

        Set<String> fieldsToFind = new HashSet<>();
        String[] fields = line.split(",");
        Set<String> existingFields = Arrays.stream(Order.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        for (String field : fields) {
            if (existingFields.contains(field)) {
                fieldsToFind.add(field);
            } else {
                throw new ServiceException(String.format("Unrecognized field : %s", field), "67");
            }
        }
        return fieldsToFind;
    }
}
