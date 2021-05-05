package com.epam.esm.service.search.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;

import java.util.List;
import java.util.stream.Collectors;

public class SearchGiftCertificateByTags implements SearchGiftCertificateService {
    private final List<Tag> tags;

    public SearchGiftCertificateByTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public List<GiftCertificate> search(GiftCertificateService service) {
        Tag firstTag = tags.get(0);
        List<GiftCertificate> certificates = service.findByTagId(firstTag);
        tags.remove(0);
        if (!tags.isEmpty()) {
            certificates.removeIf(certificate ->!certificate.getTags().containsAll(tags));
        }
        service.readTagNames(certificates);
        return certificates;
    }
}
