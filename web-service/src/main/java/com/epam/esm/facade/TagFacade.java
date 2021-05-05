package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Tag;

/**
 * Defines methods for facade layout for Tag class.
 * Methods supposed to wrap results in JsonResult class.
 */
public interface TagFacade {
    JsonResult<Tag> getTag(int id);

    JsonResult<Tag> save(Tag tag);

    JsonResult<Tag> delete(int id);

    JsonResult<Tag> getAllTags();

    JsonResult<Tag> getBestBuyerMostWidelyTag();

}
