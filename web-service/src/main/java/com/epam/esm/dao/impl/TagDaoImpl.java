package com.epam.esm.dao.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {
    private static Logger logger = LogManager.getLogger(TagDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    private static final String CREATE = "INSERT INTO tag (name) values(?)";
    private static final String READ = "SELECT * FROM tag WHERE id = ?";
    private static final String READ_BY_PART_NAME = "SELECT * FROM tag WHERE name  LIKE CONCAT('%', ?, '%')";
    private static final String READ_NAME = "SELECT name FROM tag WHERE id = ?";
    private static final String DELETE = "DELETE FROM tag WHERE id = ?";
    private static final String READ_ALL = "SELECT * FROM tag";
    private static final String READ_CERTIFICATES_BY_TAG = "SELECT * FROM certificate_tag where tag_id = ?";
    private static final String CHECK_IF_EXIST = "SELECT 1 FROM tag WHERE name=?";
    private static final String READ_BY_NAME = "SELECT id FROM tag WHERE name=?";

    @Override
    @NotNull
    public void create(@NotNull Tag tag) throws DaoException {
        try {
            em.persist(tag);
            logger.debug("New tag created with id={}, name={}", tag.getId(), tag.getName());

        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not create new Tag. Name = %s", tag.getName()), "11", e);
        }

    }

    @Override
    public boolean checkIfExist(String name) throws DaoException {
        try {
            Query query = em.createQuery("SELECT 1 FROM tag t WHERE t.name = :name");
            query.setParameter("name", name);
            query.getSingleResult();
            return true;
        }catch (NoResultException e1) {
            return false;
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not check if tag with name = %s exist", name), "19", e);
        }
    }

    @Override
    public void readIdByName(Tag tag) throws DaoException {
        try {
            Query query = em.createQuery("SELECT t.id FROM tag t WHERE t.name = :name");
            query.setParameter("name", tag.getName());
            long id = (long) query.getSingleResult();
            tag.setId((int) id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException(String.format("Tag with name = %s does not exist", tag.getName()), "28", e);
        } catch (DataAccessException e) {
            throw new DaoException(String.format("Can not check if tag with name = %s exist", tag.getName()), "19", e);
        }
    }

    @Override
    @Nullable
    public Tag read(@NotNull Integer id) throws DaoException {
        try {
            Tag tag = em.find(Tag.class, id);
            if (tag == null) {
                throw new DaoException(String.format("Tag with id = %s not found.", id), "404");
            }
            return tag;
        } catch (IllegalArgumentException e) {
            throw new DaoException(String.format("Can not read Tag (id = %s)", id), "12", e);
        }
    }

    private static final String READ_MOST_WIDELY_USED_TAG_OF_USER = "SELECT tag_id AS id, count(tag_id) AS total  FROM certificate_tag AS t JOIN user_order AS o ON t.certificate_id = o.certificate_id WHERE o.user_id = ? GROUP BY t.tag_id ORDER BY total LIMIT 1";

    @Override
    public Tag readUsersMostWidelyTag(Integer userId) throws DaoException {
        try {
            Query query = em.createNativeQuery(READ_MOST_WIDELY_USED_TAG_OF_USER);
            query.setParameter(1, userId);
            Object[] objects = (Object[]) query.getSingleResult();
            int id = (int) objects[0];
            return read(id);
        } catch (NoResultException e1) {
            throw new DaoException(String.format("Tag for user id = %s not found.", userId), "404");
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not read Tag for user id = %s", userId), "40", e);
        }
    }

    @Override
    public void delete(@NotNull Integer id) throws DaoException {
        try {
            Tag tag = read(id);
            em.remove(tag);
            logger.debug("Deleted tag with id={}", id);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Can not delete Tag (id = %s)", id), "14", e);
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
            throw new DaoException("Can not read all Tag", "15", e);
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
            throw new DaoException("Can't count tags.", "38");
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
        } catch (DataAccessException e) {
            throw new DaoException(String.format("No tags with name = %s)", tagName), "21", e);
        }
    }
}
