package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

/**
 * Defines methods for dao layout for GiftCertificate class.
 */
public interface GiftCertificateDao {

    @NotNull
    void create(@NotNull GiftCertificate entity) throws DaoException;

    @NotNull
    GiftCertificate read(@NotNull Integer id) throws DaoException;

    void update(@NotNull GiftCertificate entity) throws DaoException;

    void delete(@NotNull Integer id) throws DaoException;

    @NotNull
    List<GiftCertificate> readAllActive(int page, int size) throws DaoException;

    @NonNull
    Integer countAllActive() throws DaoException;

    /**
     * Reads GiftCertificates by tags.
     *
     * @param tags - certificate must have at least one of these tags.
     * @return - list of found certificates
     * @throws DaoException if any exception occur while receiving data.
     */
    @NotNull
    List<GiftCertificate> readByTags(@NotNull List<Tag> tags) throws DaoException;


    /**
     * Reads GiftCertificates by name.
     *
     * @param name - certificate's name or part of name.
     * @return - list of found certificates
     * @throws DaoException if any exception occur while receiving data.
     */

    @NonNull
    List<GiftCertificate> readByPartName(@NonNull String name) throws DaoException;

    /**
     * Reads GiftCertificates by name and tag.
     *
     * @param name - certificate's name or part of name.
     * @param tags - certificate must have at least one of these tags.
     * @return - list of found certificates
     * @throws DaoException if any exception occur while receiving data.
     */
    @NotNull
    List<GiftCertificate> readByNameAndTagName(@NotNull String name, @NotNull List<Tag> tags) throws DaoException;
}
