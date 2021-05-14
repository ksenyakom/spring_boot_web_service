package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller class for Tag
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagFacade tagFacade;

    @GetMapping()
    public JsonResult<Tag> index(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "perPage", defaultValue = "5") int perPage,
                                 @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) throws ServiceException {
        return tagFacade.getAllTags(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<Tag> show(@PathVariable("id") int id) throws ServiceException {
        return tagFacade.getTag(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<Tag> create(@Valid @RequestBody Tag tag, BindingResult result) throws ServiceException {
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }
        return tagFacade.save(tag);
    }

    @DeleteMapping("/{id}")
    public JsonResult<Tag> delete(@PathVariable("id") int id) throws ServiceException {
        return tagFacade.delete(id);
    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getDefaultMessage()).append("."));
        return sb.toString();
    }
}
