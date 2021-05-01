package com.epam.esm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends Entity {
    private User user;
    private GiftCertificate certificate;
    private LocalDateTime createDate;
    private BigDecimal price;
    private boolean isActive;

    public Order() {}

    public Order(Integer id) {
        super(id);
    }

    public Order(User user, GiftCertificate certificate, LocalDateTime createDate, BigDecimal price) {
        this.user = user;
        this.certificate = certificate;
        this.createDate = createDate;
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificate certificate) {
        this.certificate = certificate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return isActive == order.isActive &&
                Objects.equals(user, order.user) &&
                Objects.equals(certificate, order.certificate) &&
                Objects.equals(createDate, order.createDate) &&
                (price.compareTo(order.price) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, certificate, createDate, price, isActive);
    }
}
