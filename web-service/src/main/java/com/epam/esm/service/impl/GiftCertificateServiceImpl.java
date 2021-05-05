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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            GiftCertificate certificate = giftCertificateDao.read(id);
            readTagName(certificate.getTags());
            return certificate;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    public List<GiftCertificate> findByTagId(Tag tag) {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readByTags(Collections.singletonList(tag));
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findAll(int page, int size) throws ServiceException {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readAllActive(page, size);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public Integer countAll() throws ServiceException {
        try {
            return  giftCertificateDao.countAllActive();
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
                certificate.setIsActive(true);

                certificate.setId(giftCertificateDao.create(certificate));
                readTagName(certificate.getTags());
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
            tag.setId(tagDao.create(tag));
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
            List<GiftCertificate> certificates = giftCertificateDao.readByTags(tags);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findByName(@NonNull String name) {
        try {
            List<GiftCertificate> certificates = giftCertificateDao.readByName(name);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> findByNameAndTagName(@NonNull String name, @NonNull String tagName) {
        try {
            List<Tag> tags = tagDao.readByPartName(tagName);
            List<GiftCertificate> certificates = giftCertificateDao.readByNameAndTagName(name, tags);
            readTagNames(certificates);
            return certificates;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

    private void readTagName(List<Tag> tags) throws DaoException {
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getName() == null) {
                    tagDao.read(tag);
                }
            }
        }
    }

    @Override
    public void readTagNames(List<GiftCertificate> certificates)  {
        try {
            List<Tag> tags = certificates.stream()
                    .filter(certificate -> certificate.getTags() != null)
                    .flatMap(certificate -> certificate.getTags().stream())
                    .distinct()
                    .collect(Collectors.toList());
            readTagName(tags);
        }catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e.getCause());
        }
    }

}
