package com.epam.esm.facade.impl;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.facade.impl.TagFacadeImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagFacadeImplTest {
    @Mock
    TagService tagService;

    TagFacade tagFacade;

    @BeforeEach
    void setUp() {
        tagFacade = new TagFacadeImpl(tagService);
    }

    @Test
    void getTag() {
        given(tagService.findById(anyInt())).willReturn(new Tag());
        JsonResult<Tag> jsonResult = tagFacade.getTag(anyInt() );

        assertAll(() -> {
            assertEquals(1, jsonResult.getResult().size());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void save() {
        JsonResult<Tag> jsonResult = tagFacade.save(new Tag());

        assertAll(() -> {
            assertEquals(1, jsonResult.getResult().size());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void delete() {
        JsonResult<Tag> jsonResult = tagFacade.delete(anyInt());

        assertAll(() -> {
            assertNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }

    @Test
    void getAllTags() {
        given(tagService.findAll(1, 5)).willReturn(new ArrayList<>());
        JsonResult<Tag> jsonResult = tagFacade.getAllTags(1, 5, true);

        assertAll(() -> {
            assertNotNull(jsonResult.getResult());
            assertTrue(jsonResult.isSuccess());
        });
    }
}