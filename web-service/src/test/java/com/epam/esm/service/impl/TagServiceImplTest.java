package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.OrderDao;
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
    private OrderDao orderDao;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagDao, orderDao);
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
        given(tagDao.readAll(anyInt(), anyInt())).willReturn(tags);
        List<Tag> actual = tagService.findAll(anyInt(), anyInt());

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(tagDao.readAll(anyInt(), anyInt())).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> tagService.findAll(anyInt(), anyInt()));
    }

}