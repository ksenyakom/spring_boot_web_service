package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static Logger logger = LogManager.getLogger(GiftCertificateDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    private static final String READ_BY_NAME_AND_TAG_ID = "SELECT name, description, price, duration, create_date,  last_update_date, is_active, certificate_id as id, operation, timestamp FROM gift_certificate c join certificate_tag t on c.id = t.certificate_id  WHERE c.name LIKE CONCAT('%', ?, '%') AND t.tag_id = ? AND is_active = true";
    private static final String READ_BY_TAG_ID =
            "SELECT name, description, price, duration, create_date,  last_update_date, is_active, certificate_id as id, operation, timestamp  FROM gift_certificate c join certificate_tag t on c.id = t.certificate_id  WHERE t.tag_id = ? AND is_active = true";


    @Override
    @NonNull
    public void create(@NonNull GiftCertificate certificate) throws DaoException {
        try {
            em.persist(certificate);
            logger.debug("New certificate created with id={}", certificate.getId());
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not create new GiftCertificate. Name = %s ", certificate.getName()), "01", e);
        } catch (ConstraintViolationException e) {
            throw new DaoException("Can not create new GiftCertificate, constraint violations", "43", e);
        }
    }

    @Override
    @NonNull
    public GiftCertificate read(@NonNull Integer id) throws DaoException {
        try {
            GiftCertificate giftCertificate = em.find(GiftCertificate.class, id);
            if (giftCertificate == null) {
                throw new DaoException(String.format("GiftCertificate with id = %s not found.", id), "404");
            }
            return giftCertificate;
        } catch (IllegalArgumentException e) {
            throw new DaoException(String.format("Can not read GiftCertificate (id = %s).", id), "02", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readAllActive(int page, int size) throws DaoException {
        String jpql = "SELECT c FROM gift_certificate c WHERE c.isActive = true";
        Query query = em.createQuery(jpql);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    @NonNull
    public Integer countAllActive() throws DaoException {
        try {
            Query query = em.createQuery("SELECT count(c) FROM gift_certificate c WHERE c.isActive = true");
            long count = (long) query.getSingleResult();
            return (int) count;
        } catch (PersistenceException e) {
            throw new DaoException("Can't count active giftCertificates.", "33");
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readByTags(@NonNull List<Tag> tags) throws DaoException {
        try {
            Query query = em.createNativeQuery(READ_BY_TAG_ID, GiftCertificate.class);

            Set<GiftCertificate> certificates = new HashSet<>();
            for (Tag tag : tags) {
                query.setParameter(1, tag.getId());
                certificates.addAll(query.getResultList());
            }
            return new ArrayList<>(certificates);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not read certificates by tags (tag = %s)", tags), "22", e);
        }
    }

    @Override
    @NonNull
    public List<GiftCertificate> readByPartName(@NonNull String name) throws DaoException {
        try {
            String jpql = "SELECT c FROM gift_certificate c WHERE c.name LIKE CONCAT('%', :name, '%') AND c.isActive = true";

            Query query = em.createQuery(jpql);
            query.setParameter("name", name);
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not read certificates by name (name = %s)", name), "23", e);
        }
    }


    @Override
    @NonNull
    public List<GiftCertificate> readByNameAndTagName(@NonNull String name, @NonNull List<Tag> tags) throws DaoException {
        try {
            Query query = em.createNativeQuery(READ_BY_NAME_AND_TAG_ID, GiftCertificate.class);
            query.setParameter(1, name);
            Set<GiftCertificate> certificates = new HashSet<>();
            for (Tag tag : tags) {
                query.setParameter(2, tag.getId());
                certificates.addAll(query.getResultList());
            }
            return new ArrayList<>(certificates);
        } catch (PersistenceException e) {
            throw new DaoException("Can not read certificates by tags and name", "35", e);
        }
    }

    @Override
    public void update(@NonNull GiftCertificate certificate) throws DaoException {
        try {
            GiftCertificate existingCertificate = read(certificate.getId());
            certificate.setCreateDate(existingCertificate.getCreateDate());
            em.merge(certificate);
            logger.debug("Certificate was updated with id={}", certificate.getId());
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not update GiftCertificate (id = %s)", certificate.getId()), "03", e);
        } catch (ConstraintViolationException e) {
            throw new DaoException("Can not create new GiftCertificate, constraint violations", "43", e);
        }
    }

    @Override
    public void delete(@NonNull Integer id) throws DaoException {
        try {
            GiftCertificate giftCertificate = read(id);
            em.remove(giftCertificate);
            logger.debug("Certificate was deleted(isActive=false) with id={}", id);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not delete GiftCertificate (id = %s)", id), "04", e);
        }
    }

}
