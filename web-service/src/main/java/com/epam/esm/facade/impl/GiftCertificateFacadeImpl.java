package com.epam.esm.facade.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.Metadata;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CopyFields;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;
import com.epam.esm.service.search.impl.SearchGiftCertificateByNameAndTagName;
import com.epam.esm.service.search.impl.SearchGiftCertificateByTags;
import com.epam.esm.service.sort.SortGiftCertificateService;
import com.epam.esm.service.sort.impl.SortByNameAndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateFacadeImpl implements GiftCertificateFacade {

    private final GiftCertificateService giftCertificateService;
    private final CopyFields copyFields;

    @Autowired
    public GiftCertificateFacadeImpl(GiftCertificateService giftCertificateService, CopyFields copyFields) {
        this.giftCertificateService = giftCertificateService;
        this.copyFields = copyFields;
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
    public JsonResult<GiftCertificate> partUpdate(GiftCertificate certificate) {
        GiftCertificate actual = giftCertificateService.findById(certificate.getId());
        copyFields.copyNotEmptyFields(actual, certificate);
        giftCertificateService.save(actual);
        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(actual))
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
        SearchGiftCertificateByNameAndTagName searchCertificate = new SearchGiftCertificateByNameAndTagName(name, tagName);
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
    public JsonResult<GiftCertificate> search(List<Tag> tagsList) {
        SearchGiftCertificateService searchCertificate = new SearchGiftCertificateByTags(tagsList);
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
    public JsonResult<GiftCertificate> getAllCertificates(int page, int perPage, boolean includeMetadata) {
        List<GiftCertificate> certificates = giftCertificateService.findAll(page, perPage);
        Metadata metadata = fillMetadata(includeMetadata, page, perPage);

        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificates)
                .withMetadata(metadata)
                .build();
    }

    private Metadata fillMetadata (boolean includeMetadata, int page, int perPage) {
        if (includeMetadata) {
            int totalFound = giftCertificateService.countAll();
            Metadata metadata = new Metadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + 1)
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = metadata.getPageCount();
            metadata.add(linkTo(methodOn(GiftCertificateController.class).index(page, perPage, includeMetadata)).withSelfRel());
            metadata.add(linkTo(methodOn(GiftCertificateController.class).index(1, perPage, includeMetadata)).withRel("first"));
            metadata.add(linkTo(methodOn(GiftCertificateController.class).index(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            metadata.add(linkTo(methodOn(GiftCertificateController.class).index(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            metadata.add(linkTo(methodOn(GiftCertificateController.class).index(pageCount, perPage, includeMetadata)).withRel("last"));
            return metadata;
        }
       return null;
    }
}
