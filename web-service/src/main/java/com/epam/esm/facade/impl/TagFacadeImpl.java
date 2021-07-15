package com.epam.esm.facade.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.PageMetadata;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.model.Permission;
import com.epam.esm.model.Tag;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagFacadeImpl implements TagFacade {
    private static final SimpleGrantedAuthority TAGS_WRITE_AUTHORITY = new SimpleGrantedAuthority(Permission.TAGS_WRITE.getPermission());

    private final TagService tagService;

    @Autowired
    public TagFacadeImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public JsonResult<Tag> getTag(int id) {
        Tag tag = tagService.findById(id);
        addHateoasLinks(tag);

        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tag))
                .build();
    }

    @Override
    public JsonResult<Tag> getBestBuyerMostWidelyTag() {
        Tag tag = tagService.findBestBuyerMostWidelyTag();
        addHateoasLinks(tag);

        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tag))
                .build();
    }

    @Override
    public JsonResult<Tag> save(Tag tag) {
        tagService.save(tag);
        addHateoasLinks(tag);

        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tag))
                .build();
    }

    @Override
    public JsonResult<Tag> delete(int id) {
        tagService.delete(id);
        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .build();
    }

    @Override
    public JsonResult<Tag> getAllTags(int page, int perPage, boolean includeMetadata) {
        List<Tag> tags = tagService.findAll(page, perPage);
        tags.forEach(tag -> addHateoasLinks(tag));
        int totalFound = tagService.countAll();
        PageMetadata pageMetadata = fillPageMetadata(includeMetadata, page, perPage, totalFound);

        return new JsonResult.Builder<Tag>()
                .withSuccess(true)
                .withResult(tags)
                .withMetadata(pageMetadata)
                .build();
    }

    private void addHateoasLinks(Tag tag) {
        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(TAGS_WRITE_AUTHORITY)) {
            tag.add(linkTo(methodOn(TagController.class).delete(tag.getId())).withRel("delete"));
        }

    }

    private PageMetadata fillPageMetadata(boolean includeMetadata, int page, int perPage, int totalFound) {
        if (includeMetadata) {
            PageMetadata pageMetadata = new PageMetadata.Builder()
                    .withPage(page)
                    .withPerPage(perPage)
                    .withPageCount(totalFound / perPage + (totalFound % perPage == 0 ? 0 : 1))
                    .withTotalCount(totalFound)
                    .build();
            int pageCount = pageMetadata.getPageCount();
            pageMetadata.add(linkTo(methodOn(TagController.class).getAll(page, perPage, includeMetadata)).withSelfRel());
            pageMetadata.add(linkTo(methodOn(TagController.class).getAll(1, perPage, includeMetadata)).withRel("first"));
            pageMetadata.add(linkTo(methodOn(TagController.class).getAll(page < 2 ? 1 : page - 1, perPage, includeMetadata)).withRel("previous"));
            pageMetadata.add(linkTo(methodOn(TagController.class).getAll(page >= pageCount ? pageCount : page + 1, perPage, includeMetadata)).withRel("next"));
            pageMetadata.add(linkTo(methodOn(TagController.class).getAll(pageCount, perPage, includeMetadata)).withRel("last"));


            return pageMetadata;
        }
        return null;
    }
}
