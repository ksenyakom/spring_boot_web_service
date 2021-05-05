package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final UserDao userDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao) {
        this.tagDao = tagDao;
        this.userDao = userDao;
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
    public List<Tag> findAll() throws ServiceException {
        try {
            return tagDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @Nullable
    public Tag findBestBuyerMostWidelyTag() throws ServiceException {
        try {
            User user = userDao.readBestBuyer();
            if (user == null) {
                return null;
            }
            Tag tag = tagDao.readUsersMostWidelyTag(user.getId());

            return tag;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void save(Tag tag) throws ServiceException {
        try {
            if (tag.getId() != null) {
                throw new ServiceException("You can not choose id for tag", "29");
            }
            if (tagDao.checkIfExist(tag.getName())) {
                throw new ServiceException(String.format("Tag with name = %s already exist", tag.getName()), "19");
            }
            tag.setId(tagDao.create(tag));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }
}
