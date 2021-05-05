package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

public class Metadata extends RepresentationModel<Metadata> {
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
        private Metadata metadata;

        public Builder() {
            metadata = new Metadata();
        }

        public Builder withPage(int page) {
            metadata.page = page;
            return this;
        }
        public Builder withPerPage(int perPage) {
            metadata.perPage = perPage;
            return this;
        }

        public Builder withPageCount(int pageCount) {
            metadata.pageCount = pageCount;
            return this;
        }

        public Builder withTotalCount(int totalCount) {
            metadata.totalCount = totalCount;
            return this;
        }

        public Metadata build() {
            return metadata;
        }
    }
}
