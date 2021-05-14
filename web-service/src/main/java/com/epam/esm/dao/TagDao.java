package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Defines methods for dao layout for Tag class.
 */
public interface TagDao {

    void create(Tag tag) throws DaoException;

    boolean checkIfExist(String name) throws DaoException;

    void readIdByName(Tag tag) throws DaoException;

    Tag read(Integer id) throws DaoException;

    void delete(Integer id) throws DaoException;

    List<Tag> readAll(int page, int size) throws DaoException;

    @NonNull
    Integer countAll() throws DaoException;


    /**
     * Reads Tag by name.
     *
     * @param tagName - tag's name or part of name.
     * @return - list of found tags.
     * @throws DaoException if any exception occur while receiving data.
     */
    List<Tag> readByPartName(String tagName) throws DaoException;

    Tag readUsersMostWidelyTag(Integer id) throws DaoException;
}
