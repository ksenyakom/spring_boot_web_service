package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;

    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    @NonNull
    public GiftCertificate findById(Integer id) throws ServiceException {
        try {
            return giftCertificateDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<GiftCertificate> findByTagId(Tag tag) {
        try {
            return giftCertificateDao.readByTags(Collections.singletonList(tag));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findAll(int page, int size) throws ServiceException {
        try {
            return giftCertificateDao.readAllActive(page, size);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public Integer countAll() throws ServiceException {
        try {
            return giftCertificateDao.countAllActive();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void save(@NonNull GiftCertificate certificate) throws ServiceException {
        try {
            createTagIfNotExist(certificate.getTags());
            if (certificate.getId() == null) {
                certificate.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
                certificate.setActive(true);
                giftCertificateDao.create(certificate);
            } else {
                certificate.setLastUpdateDate(LocalDateTime.now(ZoneOffset.UTC));
                giftCertificateDao.update(certificate);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    private void createTagIfNotExist(List<Tag> tags) throws DaoException {
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getId() == null) {
                    readOrCreate(tag);
                }
            }
        }
    }

    private void readOrCreate(Tag tag) throws DaoException {
        if (tagDao.checkIfExist(tag.getName())) {
            tagDao.readIdByName(tag);
        } else {
            tagDao.create(tag);
        }
    }

    @Transactional
    @Override
    public void delete(@NonNull Integer id) throws ServiceException {
        try {
            giftCertificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findByTagName(@NonNull String tagName) {
        try {
            List<Tag> tags = tagDao.readByPartName(tagName);
            return giftCertificateDao.readByTags(tags);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findByName(@NonNull String name) {
        try {
            return giftCertificateDao.readByPartName(name);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findByNameAndTagName(@NonNull String name, @NonNull String tagName) {
        try {
            List<Tag> tags = tagDao.readByPartName(tagName);
            return giftCertificateDao.readByNameAndTagName(name, tags);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

}
