package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.SearchParams;
import com.epam.esm.service.CopyFields;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GiftCertificateFacadeImplTest {

    GiftCertificateFacade giftCertificateFacade;
    @Mock
    GiftCertificateService giftCertificateService;

    @BeforeEach
    void setUp() {
        CopyFields copyFields = new CopyFields();
        giftCertificateFacade = new GiftCertificateFacadeImpl(giftCertificateService, copyFields);
    }

    @Test
    void getCertificate() {
        given(giftCertificateService.findById(anyInt())).willReturn(new GiftCertificate());
        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.getCertificate(anyInt());

        assertAll(() -> {
            assertEquals(1, jsonResult.getResult().size());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void save() {
        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.save(new GiftCertificate());

        assertAll(() -> {
            assertEquals(1, jsonResult.getResult().size());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void delete() {
        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.delete(anyInt());

        assertAll(() -> {
            assertNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void search() {
        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.search(new SearchParams(), 1, 5, true);

        assertAll(() -> {
            assertNotNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void getAllCertificates() {
        given(giftCertificateService.findAll(1, 5)).willReturn(new ArrayList<>());
        JsonResult<GiftCertificate> jsonResult = giftCertificateFacade.getAllCertificates(1, 5,true);

        assertAll(() -> {
            assertNotNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }
}