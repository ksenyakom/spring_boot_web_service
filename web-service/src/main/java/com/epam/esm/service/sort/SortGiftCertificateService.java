package com.epam.esm.service.sort;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

/**
 * Interface for sorting of GiftCertificate.
 */
public interface SortGiftCertificateService {
    void sort(List<GiftCertificate> certificates);
}
