package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    private static Logger logger = LogManager.getLogger(TagDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @NotNull
    public void create(@NotNull Tag tag) throws DaoException {
        try {
            em.persist(tag);
            logger.debug("New tag created with id={}, name={}", tag.getId(), tag.getName());

        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not create new Tag. Name = %s", tag.getName()), "50011", e);
        } catch (ConstraintViolationException e) {
            throw new DaoException(String.format("Can not create new Tag without name. Name = %s", tag.getName()), "42241", e);
        }
    }

    @Override
    public boolean checkIfExist(String name) throws DaoException {
        try {
            Query query = em.createQuery("SELECT 1 FROM tag t WHERE t.name = :name");
            query.setParameter("name", name);
            query.getSingleResult();
            return true;
        } catch (NoResultException e1) {
            return false;
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not check if tag with name = %s exist", name), "50085", e);
        }
    }

    @Override
    public void readIdByName(Tag tag) throws DaoException {
        try {
            Query query = em.createQuery("SELECT t.id FROM tag t WHERE t.name = :name");
            query.setParameter("name", tag.getName());
            int id = (int) query.getSingleResult();
            tag.setId(id);
        } catch (NoResultException e) {
            throw new DaoException(String.format("Tag with name = %s does not exist", tag.getName()), "40428", e);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not check if tag with name = %s exist", tag.getName()), "50085", e);
        }
    }

    @Override
    @Nullable
    public Tag read(@NotNull Integer id) throws DaoException {
        try {
            Tag tag = em.find(Tag.class, id);
            if (tag == null) {
                throw new DaoException(String.format("Tag with id = %s not found.", id), "40400");
            }
            return tag;
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not read Tag (id = %s)", id), "50012", e);
        }
    }


    @Override
    public Tag readBestBuyerMostWidelyUsedTag() throws DaoException {
        try {
            Query query = em.createNativeQuery("select test_function()");
            Integer id = (Integer) query.getSingleResult();
            return read(id);
        } catch (NoResultException e1) {
            throw new DaoException("Best buyer most widely used tag not found.", "40400");
        } catch (PersistenceException e) {
            throw new DaoException("Can not read best buyer most widely used Tag", "40040", e);
        }
    }

    @Override
    public void delete(@NotNull Integer id) throws DaoException {
        try {
            Tag tag = read(id);
            em.remove(tag);
            logger.debug("Deleted tag with id={}", id);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not delete Tag (id = %s)", id), "50014", e);
        }
    }

    @Override
    @Nullable
    public List<Tag> readAll(int page, int size) throws DaoException {
        try {
            Query query = em.createQuery("SELECT t FROM tag t");
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Can not read all Tag", "50015", e);
        }
    }

    @Override
    @NonNull
    public Integer countAll() throws DaoException {
        try {
            Query query = em.createQuery("SELECT count(t) FROM tag t");
            long count = (long) query.getSingleResult();
            return (int) count;
        } catch (PersistenceException e) {
            throw new DaoException("Can't count tags.", "50038");
        }
    }

    @Override
    @Nullable
    public List<Tag> readByPartName(@NotNull String tagName) throws DaoException {
        try {
            String jpql = "SELECT t FROM tag t WHERE t.name LIKE CONCAT('%', :name, '%')";
            Query query = em.createQuery(jpql);
            query.setParameter("name", tagName);
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(String.format("No tags with name = %s)", tagName), "40421", e);
        }
    }
}
