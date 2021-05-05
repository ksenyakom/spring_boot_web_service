package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagDao tagDao;
    @Mock
    private UserDao userDao;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagDao, userDao);
    }

    @Test
    void findById() throws DaoException {
        int id = 1;
        String tagName = "health";
        given(tagDao.read(id)).willReturn(new Tag(id, tagName));
        Tag actual = tagService.findById(id);

        assertAll("tags should be equal",
                () -> {
                    assertEquals(id, (int) actual.getId());
                    assertEquals(tagName, actual.getName());
                }
        );
    }

    @Test
    void findByIdException() throws DaoException {
        given(tagDao.read(anyInt())).willThrow(DaoException.class);
        Integer id = anyInt();

        assertThrows(ServiceException.class, () -> tagService.findById(id));
    }


    @Test
    void findAll() throws DaoException {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag());
        tags.add(new Tag());
        given(tagDao.readAll()).willReturn(tags);
        List<Tag> actual = tagService.findAll();

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(tagDao.readAll()).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> tagService.findAll());
    }

    @Test
    void save() throws DaoException {
        Tag tag = new Tag();
        int id = 1;
        given(tagDao.create(tag)).willReturn(id);
        tagService.save(tag);

        assertAll(() -> {
                    assertEquals(id, tag.getId());
                }
        );
    }

    @Test
    void saveException() throws DaoException {
        given(tagDao.create(any(Tag.class))).willThrow(DaoException.class);
        Tag tag = new Tag();

        assertThrows(ServiceException.class, () -> tagService.save(tag));
    }

}