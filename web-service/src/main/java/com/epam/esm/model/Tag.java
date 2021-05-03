package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Tag extends Entity {

    @NotEmpty(message = "empty field")
    @Size(min = 2, max = 255, message = "invalid length, must be from 2 to 255")
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Tag(Integer tagId) {
        super(tagId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
