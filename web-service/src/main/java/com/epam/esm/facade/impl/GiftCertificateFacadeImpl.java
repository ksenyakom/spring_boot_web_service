package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.impl.SearchByNameAndTagName;
import com.epam.esm.service.sort.SortGiftCertificateService;
import com.epam.esm.service.sort.impl.SortByNameAndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GiftCertificateFacadeImpl implements GiftCertificateFacade {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateFacadeImpl(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @NonNull
    public JsonResult<GiftCertificate> getCertificate(int id) {
        GiftCertificate certificate = giftCertificateService.findById(id);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(certificate))
                .build();
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> save(GiftCertificate certificate) {
        giftCertificateService.save(certificate);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(certificate))
                .build();
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> delete(int id) {
        giftCertificateService.delete(id);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .build();
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> search(@Nullable String name, @Nullable String tagName) {
        SearchByNameAndTagName searchCertificate = new SearchByNameAndTagName(name, tagName);
        List<GiftCertificate> certificateList = searchCertificate.search(giftCertificateService);
        String massage = certificateList.isEmpty()
                ? "No GiftCertificates found for search parameters."
                : String.format("Found results: %s.", certificateList.size());
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificateList)
                .withMessage(massage)
                .build();
    }

    @Override
    public void sort(@Nullable String sortByName, @Nullable String sortByDate, @NonNull List<GiftCertificate> certificates) {
        SortGiftCertificateService sortCertificate = new SortByNameAndDate(sortByName, sortByDate);
        sortCertificate.sort(certificates);
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> getAllCertificates() {
        List<GiftCertificate> certificates = giftCertificateService.findAll();
        String message = String.format("Found results: %s.",certificates.size());
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withMessage(message)
                .withResult(certificates)
                .build();
    }
}
