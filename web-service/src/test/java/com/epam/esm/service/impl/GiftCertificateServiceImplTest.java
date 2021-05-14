package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
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
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDao dao;
    @Mock
    private TagDao tagDao;
    private GiftCertificateService service;

    @BeforeEach
    void setUp() {
        service = new GiftCertificateServiceImpl(dao, tagDao);
    }

    @Test
    void findById() throws DaoException {
        int id = 3;
        given(dao.read(id)).willReturn(new GiftCertificate(id));
        GiftCertificate certificate = service.findById(id);

        assertEquals(id, certificate.getId());
    }

    @Test
    void findByIdException() throws DaoException {
        given(dao.read(anyInt())).willThrow(DaoException.class);
        Integer id = anyInt();

        assertThrows(ServiceException.class, () -> service.findById(id));
    }

    @Test
    void findAll() throws DaoException {
        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(new GiftCertificate());
        certificates.add(new GiftCertificate());
        given(dao.readAllActive(anyInt(), anyInt())).willReturn(certificates);
        List<GiftCertificate> actual = service.findAll(anyInt(), anyInt());

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(dao.readAllActive(anyInt(), anyInt())).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> service.findAll(anyInt(), anyInt()));
    }

    @Test
    void save() throws DaoException {
        GiftCertificate giftCertificate = new GiftCertificate();
        int id = 1;
        service.save(giftCertificate);

        assertAll(() -> {
            assertEquals(id, giftCertificate.getId());
            assertNotNull(giftCertificate.getCreateDate());
        });
    }


}