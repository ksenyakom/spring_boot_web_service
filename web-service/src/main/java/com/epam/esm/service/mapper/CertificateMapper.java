package com.epam.esm.service.mapper;

import com.epam.esm.model.GiftCertificate;

public interface CertificateMapper {
    void copyNotEmptyFields(GiftCertificate copyTo, GiftCertificate copyFrom);
}
