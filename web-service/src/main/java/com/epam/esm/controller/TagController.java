package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * Controller class for Tag
 */
@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private TagFacade tagFacade;

    private TagValidator tagValidator;

    @Autowired
    public TagController(TagFacade tagFacade, TagValidator tagValidator) {
        this.tagFacade = tagFacade;
        this.tagValidator = tagValidator;
    }

    @GetMapping()
    public JsonResult<Tag> index(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                 @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                 @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) throws ServiceException {
        return tagFacade.getAllTags(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<Tag> show(@PathVariable("id") @Min(1) Integer id) throws ServiceException {
        return tagFacade.getTag(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<Tag> create(@RequestBody Tag tag, BindingResult result) throws ServiceException {
        tagValidator.validate(tag, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }
        return tagFacade.save(tag);
    }

    @DeleteMapping("/{id}")
    public JsonResult<Tag> delete(@PathVariable("id") @Min(1) Integer id) throws ServiceException {
        return tagFacade.delete(id);
    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getCode()).append("."));
        return sb.toString();
    }
}
