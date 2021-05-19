package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.facade.GiftCertificateFacade;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for GiftCertificate
 */
@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificateController {

    @Autowired
    private GiftCertificateFacade giftCertificateFacade;

    @Autowired
    private TagService tagService;

    @Autowired
    @Qualifier("giftCertificateValidator")
    private Validator giftCertificateValidator;

    @Autowired
    @Qualifier("giftCertificatePartValidator")
    private Validator giftCertificatePartValidator;

    @Autowired
    @Qualifier("giftCertificateSearchValidator")
    private Validator searchValidator;


    @GetMapping()
    public JsonResult<GiftCertificate> index(@RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
                                             @RequestParam(value = "perPage", defaultValue = "5") @Min(1) Integer perPage,
                                             @RequestParam(value = "includeMetadata", required = false, defaultValue = "true") boolean includeMetadata) {
        return giftCertificateFacade.getAllCertificates(page, perPage, includeMetadata);
    }

    @GetMapping("/{id}")
    public JsonResult<GiftCertificate> show(@PathVariable("id") @Min(1) Integer id) {
        return giftCertificateFacade.getCertificate(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<GiftCertificate> create(@RequestBody GiftCertificate certificate, BindingResult result) {
        giftCertificateValidator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return giftCertificateFacade.save(certificate);
    }

    @PutMapping("/{id}")
    public JsonResult<GiftCertificate> update(@RequestBody GiftCertificate certificate, BindingResult result,
                                              @PathVariable("id") @Min(1) Integer id) {
        certificate.setId(id);
        giftCertificateValidator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return giftCertificateFacade.save(certificate);
    }

    @PatchMapping("/{id}")
    public JsonResult<GiftCertificate> partUpdate(@RequestBody GiftCertificate certificate, BindingResult result,
                                                  @PathVariable("id") @Min(1) Integer id) {
        certificate.setId(id);
        giftCertificatePartValidator.validate(certificate, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "20");
        }

        return giftCertificateFacade.partUpdate(certificate);
    }

    @DeleteMapping("/{id}")
    public JsonResult<GiftCertificate> delete(@PathVariable("id") @Min(1) Integer id) {

        return giftCertificateFacade.delete(id);
    }

    @GetMapping("/search")
    public JsonResult<GiftCertificate> search(@ModelAttribute SearchParams searchParams, BindingResult result) {
        searchValidator.validate(searchParams, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "24");
        }

        JsonResult<GiftCertificate> jsonResult =
                giftCertificateFacade.search(searchParams.getName(), searchParams.getTagPartName());
        Optional.ofNullable(jsonResult.getResult()).ifPresent(certificates ->
                giftCertificateFacade.sort(searchParams.getSortByName(), searchParams.getSortByDate(), certificates));

        return jsonResult;
    }

    @GetMapping(value = "/search", params = {"tags"})
    public JsonResult<GiftCertificate> searchByTags(@RequestParam("tags") String tags) {
        if (tags == null || tags.isEmpty()) {
            throw new ServiceException("No tags for search found", "50");
        }
        List<Tag> tagsList = Arrays.stream(tags.split(",")).map(Tag::new).collect(Collectors.toList());
        tagService.findByName(tagsList);

        return giftCertificateFacade.search(tagsList);
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
