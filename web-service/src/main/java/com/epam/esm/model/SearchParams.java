package com.epam.esm.model;

public class SearchParams {

    private String name;

    private String tagPartName;

    private String sortFields;

    private String sortOrder;

    public String getTagPartName() {
        return tagPartName;
    }

    public void setTagPartName(String tagPartName) {
        this.tagPartName = tagPartName;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
