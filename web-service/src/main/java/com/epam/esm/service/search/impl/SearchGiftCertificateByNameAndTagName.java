package com.epam.esm.service.search.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;

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

    public SearchGiftCertificateByNameAndTagName(String name, String tagName) {
        this.name = name;
        this.tagName = tagName;
    }

    @Override
    public List<GiftCertificate> search(GiftCertificateService giftCertificateService) {
        byte choice = checkSearchParameters();
        switch (choice) {
            case 1:
                return giftCertificateService.findByName(name);
            case 2:
                return giftCertificateService.findByTagName(tagName);
            case 3:
                return giftCertificateService.findByNameAndTagName(name, tagName);
            default:
                return giftCertificateService.findAll();
        }
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
