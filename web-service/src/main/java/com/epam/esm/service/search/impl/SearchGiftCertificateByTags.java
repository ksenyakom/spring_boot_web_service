package com.epam.esm.service.search.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;

import java.util.Collections;
import java.util.List;

public class SearchGiftCertificateByTags implements SearchGiftCertificateService {
    private final List<Tag> tags;
    private Integer totalFound;

    @Override
    public Integer getTotalFound() {
        return totalFound;
    }

    public SearchGiftCertificateByTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<GiftCertificate> search(GiftCertificateService service) {
        List<GiftCertificate> certificates = findByFirstTag(service);

        if (!tags.isEmpty() && !certificates.isEmpty()) {
            certificates.removeIf(certificate -> !certificate.getTags().containsAll(tags));
        }

       return certificates;
    }

    private List<GiftCertificate> findByFirstTag(GiftCertificateService service) {
        Tag firstTag = tags.remove(0);
        return service.findByTagId(firstTag);
    }
}
