package com.epam.esm.model;

public enum Permission {
    USERS_READ_PROFILE("users:read profile"),
    USERS_READ("users:read"),
    USERS_WRITE("users:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
