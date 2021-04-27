package com.epam.esm.service.impl;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        given(dao.readAllActive()).willReturn(certificates);
        List<GiftCertificate> actual = service.findAll();

        assertEquals(2, actual.size());
    }

    @Test
    void findAllException() throws DaoException {
        given(dao.readAllActive()).willThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> service.findAll());
    }

    @ParameterizedTest
    @CsvSource({
            "Visiting CosmoCaixa, Scientific museum in Barcelona, 49, 90"
    })
    void save(ArgumentsAccessor arguments) throws DaoException {
        GiftCertificate giftCertificate = new GiftCertificate(arguments.getString(0),
                arguments.getString(1),
                arguments.get(2, BigDecimal.class),
                arguments.getInteger(3));
        int id = 1;
        given(dao.create(giftCertificate)).willReturn(id);
        service.save(giftCertificate);

        assertAll(() -> {
            assertEquals(id, giftCertificate.getId());
            assertNotNull(giftCertificate.getCreateDate());
        });
    }

    @Test
    void saveException() throws DaoException {
        given(dao.create(any(GiftCertificate.class))).willThrow(DaoException.class);
        GiftCertificate certificate = new GiftCertificate();

        assertThrows(ServiceException.class, () -> service.save(certificate));
    }
}