package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

/**
 * Defines methods for dao layout for GiftCertificate class.
 */
public interface GiftCertificateDao {

    RowMapper<GiftCertificate> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getInt("id"));
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        Timestamp createDate = resultSet.getTimestamp("create_date");
        giftCertificate.setCreateDate(createDate == null ? null : createDate.toLocalDateTime());
        Timestamp lastUpdateDate = resultSet.getTimestamp("last_update_date");
        giftCertificate.setLastUpdateDate(lastUpdateDate == null ? null : lastUpdateDate.toLocalDateTime());
        giftCertificate.setPrice(resultSet.getBigDecimal("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setIsActive(resultSet.getBoolean("is_active"));
        return giftCertificate;
    };

    @NotNull
    Integer create(@NotNull GiftCertificate entity) throws DaoException;

    @NotNull
    GiftCertificate read(@NotNull Integer id) throws DaoException;

    @NotNull
    void read(@NotNull GiftCertificate certificate) throws DaoException;

    @NotNull
    void read(@NotNull List<GiftCertificate> certificates) throws DaoException;

    void update(@NotNull GiftCertificate entity) throws DaoException;

    void delete(@NotNull Integer id) throws DaoException;

    @NotNull
    List<GiftCertificate> readAllActive() throws DaoException;

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
    @NotNull
    List<GiftCertificate> readByName(@NotNull String name) throws DaoException;

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
