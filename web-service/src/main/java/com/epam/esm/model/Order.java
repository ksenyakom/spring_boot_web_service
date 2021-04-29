package com.epam.esm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order extends Entity {
    private User user;
    private Certificate certificate;
    private LocalDateTime dateTime;
    private BigDecimal price;

    public Order() {}

    public Order(Integer id) {
        super(id);
    }

    public Order(Integer id, User user, Certificate certificate, LocalDateTime dateTime, BigDecimal price) {
        super(id);
        this.user = user;
        this.certificate = certificate;
        this.dateTime = dateTime;
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
