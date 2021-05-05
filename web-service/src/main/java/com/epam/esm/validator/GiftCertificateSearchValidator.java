package com.epam.esm.validator;

import com.epam.esm.model.SearchParams;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        String sortByName = searchParams.getSortByName();
        String sortByDate = searchParams.getSortByDate();
        String name = searchParams.getName();
        String tagPartName = searchParams.getTagPartName();

        if (name != null && (name.length() > MAX_LENGTH || name.isEmpty())) {
            errors.rejectValue("name", "invalid length");
        }

        if (tagPartName != null && (tagPartName.length() > MAX_LENGTH || tagPartName.isEmpty())) {
            errors.rejectValue("tagPartName", "invalid length");
        }

        if (sortByDate != null && !(isAscending(sortByDate) || isDescending(sortByDate))) {
            errors.rejectValue("sortByDate", "invalid value. Can be asc or desc");
        }

        if (sortByName != null && !(isAscending(sortByName) || isDescending(sortByName))) {
            errors.rejectValue("sortByName", "invalid value. Can be asc or desc");
        }

    }

    private boolean isAscending(String sortByDate) {
        return sortByDate.equalsIgnoreCase("asc");
    }

    private boolean isDescending(String sortByDate) {
        return sortByDate.equalsIgnoreCase("desc");
    }

}


