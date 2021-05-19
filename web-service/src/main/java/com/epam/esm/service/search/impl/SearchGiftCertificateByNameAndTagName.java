package com.epam.esm.service.search.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collections;
import java.util.List;

public class SearchGiftCertificateByNameAndTagName implements SearchGiftCertificateService {
    /**
     * Name or part name of GiftCertificate.
     */
    private final String name;
    /**
     * Name or part name of Tag.
     */
    private final String tagName;
    private Integer totalFound;


    public SearchGiftCertificateByNameAndTagName(String name, String tagName) {
        this.name = name;
        this.tagName = tagName;
    }

    @Override
    public Integer getTotalFound() {
        return totalFound;
    }

    @Override
    public List<GiftCertificate> search(GiftCertificateService giftCertificateService) {
        byte choice = checkSearchParameters();
        List<GiftCertificate> certificates = null;
        switch (choice) {
            case 1:
                certificates = giftCertificateService.findByName(name);
                break;
            case 2:
                certificates = giftCertificateService.findByTagName(tagName);
                break;
            case 3:
                certificates = giftCertificateService.findByNameAndTagName(name, tagName);
                break;
            default:
                certificates = Collections.emptyList();
        }
        totalFound = certificates.size();
        return certificates;
    }

    private byte checkSearchParameters() {
        byte b = 0;
        if (name != null) { // 0001
            b |= 1;
        }
        if (tagName != null) { // 0010
            b |= 2;
        }
        return b;
    }
}
