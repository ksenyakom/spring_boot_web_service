package com.epam.esm.facade.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.PageMetadata;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CopyFields;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.search.SearchGiftCertificateService;
import com.epam.esm.service.search.impl.SearchGiftCertificateByNameAndTagName;
import com.epam.esm.service.search.impl.SearchGiftCertificateByTags;
import com.epam.esm.service.sort.SortGiftCertificateService;
import com.epam.esm.service.sort.impl.SortByFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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
        addHateoasLinks(certificate);

        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(Collections.singletonList(certificate))
                .build();
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> save(GiftCertificate certificate) {
        giftCertificateService.save(certificate);
        addHateoasLinks(certificate);

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
        addHateoasLinks(certificate);

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
    public JsonResult<GiftCertificate> search(SearchParams searchParams, Integer page, Integer perPage, boolean includeMetadata) {
        SearchGiftCertificateByNameAndTagName searchCertificate = new SearchGiftCertificateByNameAndTagName(searchParams.getName(), searchParams.getTagPartName());
        List<GiftCertificate> certificates = searchCertificate.search(giftCertificateService);

        SortGiftCertificateService sortCertificate = new SortByFields(searchParams.getSortFields(), searchParams.getSortOrder());
        sortCertificate.sort(certificates);

        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, certificates.size());
        certificates = getCertificatesForPage(certificates, page, perPage, certificates.size());
        certificates.forEach(certificate -> addHateoasLinks(certificate));

        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificates)
                .withMetadata(pageMetadata)
                .build();
    }

    @Override
    public JsonResult<GiftCertificate> search(List<Tag> tagsList, Integer page, Integer perPage, boolean includeMetadata) {
        SearchGiftCertificateService searchCertificate = new SearchGiftCertificateByTags(tagsList);
        List<GiftCertificate> certificates = searchCertificate.search(giftCertificateService);
        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, certificates.size());
        certificates = getCertificatesForPage(certificates, page, perPage, certificates.size());
        certificates.forEach(certificate -> addHateoasLinks(certificate));

        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificates)
                .withMetadata(pageMetadata)
                .build();
    }

    @Override
    @NonNull
    public JsonResult<GiftCertificate> getAllCertificates(int page, int perPage, boolean includeMetadata) {
        List<GiftCertificate> certificates = giftCertificateService.findAll(page, perPage);

        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, giftCertificateService.countAll());
        certificates.forEach(certificate -> addHateoasLinks(certificate));

        return new JsonResult.Builder<GiftCertificate>()
                .withSuccess(true)
                .withResult(certificates)
                .withMetadata(pageMetadata)
                .build();
    }

    private void addHateoasLinks(GiftCertificate certificate) {
        certificate.add(linkTo(methodOn(GiftCertificateController.class).getCertificate(certificate.getId())).withSelfRel());
        certificate.add(linkTo(methodOn(GiftCertificateController.class).delete(certificate.getId())).withRel("delete"));
    }

    private PageMetadata fillPageMetadata(boolean includeMetadata, int page, int perPage, int totalFound) {
        if (includeMetadata) {
            PageMetadata pageMetadata = new PageMetadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0 ? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = pageMetadata.getPageCount();
            pageMetadata.add(linkTo(methodOn(GiftCertificateController.class).index(page, perPage, includeMetadata)).withSelfRel());
            pageMetadata.add(linkTo(methodOn(GiftCertificateController.class).index(1, perPage, includeMetadata)).withRel("first"));
            pageMetadata.add(linkTo(methodOn(GiftCertificateController.class).index(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            pageMetadata.add(linkTo(methodOn(GiftCertificateController.class).index(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            pageMetadata.add(linkTo(methodOn(GiftCertificateController.class).index(pageCount, perPage, includeMetadata)).withRel("last"));
            return pageMetadata;
        }
        return null;
    }

    private List<GiftCertificate> getCertificatesForPage(List<GiftCertificate> certificates, Integer page, Integer perPage, Integer totalFound) {
        if (totalFound == 0) {
            return Collections.emptyList();
        }

        int fromIndex = (page - 1) * perPage;
        int toIndex = Math.min((fromIndex + perPage), totalFound);

        if (fromIndex < toIndex) {
            return certificates.subList(fromIndex, toIndex);
        } else {
            return Collections.emptyList();
        }
    }
}
