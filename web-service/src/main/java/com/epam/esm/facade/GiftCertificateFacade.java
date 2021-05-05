package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
     * Searches GiftCertificate by name and tag name.
     *
     * @param name    - name or part name of GiftCertificate name.
     * @param tagName -  name or part name of Tag name.
     * @return - found GiftCertificates wrapped in JsonResult.
     */
    @NonNull
    JsonResult<GiftCertificate> search(@Nullable String name, @Nullable String tagName);

    void sort(@Nullable String sortByName, @Nullable String sortByDate, @NonNull List<GiftCertificate> certificates);


    JsonResult<GiftCertificate> search(List<Tag> tagsList);
}

