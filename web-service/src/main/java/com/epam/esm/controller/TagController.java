package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.TagDto;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.validator.TagDtoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private TagFacade tagFacade;

    @Autowired
    private TagDtoValidator tagDtoValidator;

    @Autowired
    private ModelMapper mapper;

    @GetMapping()
    public JsonResult<Tag> getAll(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                  @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                  @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) throws ServiceException {
        return tagFacade.getAllTags(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<Tag> getTag(@PathVariable("id") @Min(1) Integer id) throws ServiceException {
        return tagFacade.getTag(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('tags:write')")
    public JsonResult<Tag> create(@RequestBody TagDto tagDto, BindingResult result) throws ServiceException {
        tagDtoValidator.validate(tagDto, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "42220");
        }
        Tag tag = mapper.map(tagDto, Tag.class);
        return tagFacade.save(tag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('tags:write')")
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
