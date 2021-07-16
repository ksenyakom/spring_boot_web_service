package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final OrderDao orderDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao, OrderDao orderDao) {
        this.tagDao = tagDao;
        this.orderDao = orderDao;
    }


    @Override
    public Tag findById(Integer id) throws ServiceException {
        try {
            return tagDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void findByName(List<Tag> tagsList) throws ServiceException {
        try {
            for (Tag tag : tagsList) {
                tagDao.readIdByName(tag);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<Tag> findAll(int page, int size) throws ServiceException {
        try {
            return tagDao.readAll(page, size);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public int countAll() {
        try {
            return tagDao.countAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @Nullable
    public Tag findBestBuyerMostWidelyTag() throws ServiceException {
        try {

            return tagDao.readBestBuyerMostWidelyUsedTag();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void save(Tag tag) throws ServiceException {
        try {
            if (tag.getId() != null) {
                throw new ServiceException("You can not choose id for tag", "40629");
            }
            if (tagDao.checkIfExist(tag.getName())) {
                throw new ServiceException(String.format("Tag with name '%s' already exist", tag.getName()), "40919");
            }
            tagDao.create(tag);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }
}
