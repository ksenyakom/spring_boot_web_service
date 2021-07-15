package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dto.SearchParams;
import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Defines methods for facade layout for GiftCertificate class.
 * Methods supposed to wrap results in JsonResult class.
 */

public interface GiftCertificateFacade {
    @NonNull
    JsonResult<GiftCertificate> getCertificate(int id);

    @NonNull
    JsonResult<GiftCertificate> save(GiftCertificate certificate);

    @NonNull
    JsonResult<GiftCertificate> delete(int id);

    @NonNull
    JsonResult<GiftCertificate> getAllCertificates(int page, int size, boolean include);

    @NonNull
    JsonResult<GiftCertificate> partUpdate(GiftCertificate certificate);
    /**
     * Searches GiftCertificate by fields from searchParams
     *
     * @return - found GiftCertificates wrapped in JsonResult.
     */
    @NonNull
    JsonResult<GiftCertificate> search(SearchParams searchParams,Integer page, Integer perPage, boolean includeMetadata);

    JsonResult<GiftCertificate> search(List<Tag> tagsList, Integer page, Integer perPage, boolean includeMetadata);
}

