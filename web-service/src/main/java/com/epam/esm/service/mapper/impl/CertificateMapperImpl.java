package com.epam.esm.service.mapper.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.mapper.CertificateMapper;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapperImpl implements CertificateMapper {
    /**
     * Copying only fields which are not null or 0.
     *
     * @param copyTo   - all not empty values are copied here.
     * @param copyFrom - object may have only one not null field.
     */
    @Override
    public void copyNotEmptyFields(GiftCertificate copyTo, GiftCertificate copyFrom) {
        boolean changeExist = false;

        if (copyFrom.getName() != null) {
            copyTo.setName(copyFrom.getName());
            changeExist = true;
        }
        if (copyFrom.getDescription() != null) {
            copyTo.setDescription(copyFrom.getDescription());
            changeExist = true;
        }
        if (copyFrom.getDuration() != 0) {
            copyTo.setDuration(copyFrom.getDuration());
            changeExist = true;
        }
        if (copyFrom.getDuration() > 0) {
            changeExist = true;
        }
        if (copyFrom.getPrice() != null) {
            copyTo.setPrice(copyFrom.getPrice());
            changeExist = true;
        }
        if (copyFrom.getTags() != null) {
            copyTo.setTags(copyFrom.getTags());
            changeExist = true;
        }

        if (!changeExist) {
            throw new ServiceException(String.format("No fields to change found for GiftCertificate with id = %s", copyTo.getId()), "40027");
        }
    }
}
