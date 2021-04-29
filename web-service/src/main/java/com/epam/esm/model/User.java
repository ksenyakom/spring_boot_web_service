package com.epam.esm.model;

import java.util.Objects;

public class User extends Entity {
    private String name;
    private String surname;
    private int age;
    private String email;
    private boolean isActive;

    public User() {
    }

    public User(Integer id) {
        super(id);
    }

    public User(Integer id, String name, String surname, int age, String email, boolean isActive) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.isActive = isActive;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return age == user.age &&
                isActive == user.isActive &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, surname, age, email, isActive);
    }
}
