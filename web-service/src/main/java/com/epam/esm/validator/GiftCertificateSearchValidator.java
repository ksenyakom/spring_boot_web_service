package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.SearchParams;
import com.epam.esm.service.ServiceException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for search parameters for GiftCertificate class.
 */
@Service
public class GiftCertificateSearchValidator implements Validator {
    private static final int MAX_LENGTH = 255;

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return SearchParams.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        SearchParams searchParams = (SearchParams) o;
        String sortFields = searchParams.getSortFields();
        String sortOrder = searchParams.getSortOrder();
        String name = searchParams.getName();
        String tagPartName = searchParams.getTagPartName();

        if (name != null && (name.length() > MAX_LENGTH || name.isEmpty())) {
            errors.rejectValue("name", "invalid length");
        }

        if (tagPartName != null && (tagPartName.length() > MAX_LENGTH || tagPartName.isEmpty())) {
            errors.rejectValue("tagPartName", "invalid length");
        }

        if (sortFields != null && sortOrder != null) {
            String[] fields = sortFields.split(",");
            validateSortFields(fields, errors);

            String[] orderAscDesc = sortOrder.split(",");
            validateOrderAscDesc(orderAscDesc, errors);

            if (fields.length != orderAscDesc.length) {
                errors.rejectValue("sortFields", String.format("Parameters number are not equal sortFields:soertOrder %s : %s", fields.length, orderAscDesc.length));
            }
        }

        if ((sortFields == null && sortOrder != null) | (sortFields != null && sortOrder == null)) {
            errors.rejectValue("sortFields", "one of parameters is null (sortFields, sortOrder), should both be null, or both not null");
        }

    }

    private void validateOrderAscDesc(String[] orderAscDesc, Errors errors) {
        for (String order : orderAscDesc) {
            if (!isAscending(order) && !isDescending(order)) {
                errors.rejectValue("sortOrder", String.format("Unrecognized value : %s, must be asc or desc", order));
            }
        }
    }

    private void validateSortFields(String[] fields, Errors errors) {
        Set<String> existingFields = Arrays.stream(GiftCertificate.class.getDeclaredFields())
                .map(Field::getName)
                .filter(field -> !field.equals("operation") && !field.equals("timestamp"))
                .collect(Collectors.toSet());

        for (String field : fields) {
            if (!existingFields.contains(field)) {
                errors.rejectValue("sortFields",String.format("Unrecognized field : %s", field));
            }
        }
    }

    private boolean isAscending(String sortByDate) {
        return sortByDate.equalsIgnoreCase("asc");
    }

    private boolean isDescending(String sortByDate) {
        return sortByDate.equalsIgnoreCase("desc");
    }


}


