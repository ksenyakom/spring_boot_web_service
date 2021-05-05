package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.List;

/**
 * Defines methods for service layout for Tag class.
 */
public interface TagService {
    Tag findById(Integer id) throws ServiceException;

    List<Tag> findAll() throws ServiceException;

    void save(Tag entity) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    Tag findBestBuyerMostWidelyTag()throws ServiceException;

    void findByName(List<Tag> tagsList)throws ServiceException;
}
