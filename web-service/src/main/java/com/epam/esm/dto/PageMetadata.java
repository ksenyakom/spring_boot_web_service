package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PageMetadata extends RepresentationModel<PageMetadata> {
    private int page;
    private int perPage;
    private int pageCount;
    private int totalCount;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public static class Builder {
        private PageMetadata pageMetadata;

        public Builder() {
            pageMetadata = new PageMetadata();
        }

        public Builder withPage(int page) {
            pageMetadata.page = page;
            return this;
        }
        public Builder withPerPage(int perPage) {
            pageMetadata.perPage = perPage;
            return this;
        }

        public Builder withPageCount(int pageCount) {
            pageMetadata.pageCount = pageCount;
            return this;
        }

        public Builder withTotalCount(int totalCount) {
            pageMetadata.totalCount = totalCount;
            return this;
        }

        public PageMetadata build() {
            return pageMetadata;
        }
    }
}
