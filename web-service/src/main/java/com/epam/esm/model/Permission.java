package com.epam.esm.model;

public enum Permission {
    USERS_READ_MY_PROFILE("users:read profile"),
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    TAGS_WRITE("tags:write"),
    CERTIFICATES_WRITE("certificates:write"),
    ORDERS_CREATE("orders:create"),
    ORDERS_READ("orders:read"),
    ORDERS_READ_BY_USER("orders:read by user"),
    ORDERS_UPDATE("orders:update"),
    ORDERS_DELETE("orders:delete"),
    MOST_WIDELY_USED_TAG("most_widely_used_tag:read");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
