package com.epam.esm.model;

public class SearchParams {

    private String name;

    private String tagPartName;

    private String sortByName;

    private String sortByDate;

    public String getTagPartName() {
        return tagPartName;
    }

    public void setTagPartName(String tagPartName) {
        this.tagPartName = tagPartName;
    }

    public String getSortByName() {
        return sortByName;
    }

    public void setSortByName(String sortByName) {
        this.sortByName = sortByName;
    }

    public String getSortByDate() {
        return sortByDate;
    }

    public void setSortByDate(String sortByDate) {
        this.sortByDate = sortByDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
