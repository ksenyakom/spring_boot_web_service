package com.epam.esm.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    USER(Stream.of(Permission.USERS_READ_MY_PROFILE,
            Permission.ORDERS_CREATE,
            Permission.ORDERS_READ).collect(Collectors.toSet())),
    ADMIN(Stream.of(Permission.USERS_READ,
            Permission.USERS_WRITE,
            Permission.TAGS_WRITE,
            Permission.CERTIFICATES_WRITE,
            Permission.ORDERS_CREATE,
            Permission.ORDERS_READ,
            Permission.ORDERS_READ_BY_USER,
            Permission.ORDERS_DELETE,
            Permission.ORDERS_UPDATE,
            Permission.MOST_WIDELY_USED_TAG).collect(Collectors.toSet()));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
