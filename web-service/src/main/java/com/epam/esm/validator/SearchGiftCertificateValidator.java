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
public class SearchGiftCertificateValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return SearchParams.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        SearchParams searchParams = (SearchParams) o;

        int maxLength = 255;
        String sortByName = searchParams.getSortByName();
        String sortByDate = searchParams.getSortByDate();
        String name = searchParams.getName();
        String tagName = searchParams.getTagName();

        if (name != null && name.length() > maxLength) {
            errors.rejectValue("name", "invalid length", "Name: invalid length");
        }

        if (tagName != null && tagName.length() > maxLength) {
            errors.rejectValue("tagName", "invalid length", "TagName: invalid length");
        }

        if (sortByDate != null && !(isAscending(sortByDate) || isDescenging(sortByDate))) {
            errors.rejectValue("sortByDate", "invalid value", "sortByDate: invalid value. Can be asc or desc");
        }


        if (sortByName != null && !(isAscending(sortByName) || isDescenging(sortByName))) {
            errors.rejectValue("sortByName", "invalid value", "sortByName: invalid value. Can be asc or desc");
        }
    }

    private boolean isAscending(String sortByDate) {
        return sortByDate.equalsIgnoreCase("asc");
    }

    private boolean isDescenging(String sortByDate) {
        return sortByDate.equalsIgnoreCase("desc");
    }

}


