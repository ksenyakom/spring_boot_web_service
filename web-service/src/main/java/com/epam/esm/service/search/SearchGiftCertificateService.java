package com.epam.esm.service.search;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;

import java.util.List;

/**
 * Interface for searching of GiftCertificate.
 */
public interface SearchGiftCertificateService {
    List<GiftCertificate> search(GiftCertificateService service);
}
