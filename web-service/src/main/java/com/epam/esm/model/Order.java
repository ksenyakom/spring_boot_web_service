package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "user_order")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private GiftCertificate certificate;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    private BigDecimal price;

    @Column(name = "is_active")
    private Boolean isActive;

    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Order(User user, GiftCertificate certificate, LocalDateTime createDate, BigDecimal price) {
        this.user = user;
        this.certificate = certificate;
        this.createDate = createDate;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
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
