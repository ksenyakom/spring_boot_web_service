package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/most_widely_used_tag")
public class UsersMostWidelyTagController {
    @Autowired
    private TagFacade tagFacade;

    @GetMapping("/best_buyer")
    public JsonResult<Tag> show() throws ServiceException {
        return tagFacade.getBestBuyerMostWidelyTag();
    }
}
