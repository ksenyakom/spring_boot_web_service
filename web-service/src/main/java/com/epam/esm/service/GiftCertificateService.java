package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

/**
 * Defines methods for service layout for GiftCertificate class.
 */

public interface GiftCertificateService {
    GiftCertificate findById(Integer id) throws ServiceException;

    List<GiftCertificate> findAll() throws ServiceException;

    void save(GiftCertificate entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    /**
     * Finds GiftCertificates by tagName.
     *
     * @param tagName - tag's name or part of name,
     *                certificate must have at least one tag
     *                with name containing this String.
     * @return - list of found certificates
     * @throws ServiceException if any exception occur while receiving data.
     */
    List<GiftCertificate> findByTagName(String tagName);

    /**
     * Finds GiftCertificates by name.
     *
     * @param name - certificate's name or part of name.
     * @return - list of found certificates
     * @throws ServiceException if any exception occur while receiving data.
     */
    List<GiftCertificate> findByName(String name);

    /**
     * Finds GiftCertificates by name and tagName.
     *
     * @param name    - certificate's name or part of name.
     * @param tagName - tag's name or part of name,
     *                certificate must have at least one tag
     *                with name containing this String.
     * @return - list of found certificates
     * @throws ServiceException if any exception occur while receiving data.
     */
    List<GiftCertificate> findByNameAndTagName(String name, String tagName);
}
