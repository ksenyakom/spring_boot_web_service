package com.epam.esm.service.sort.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.sort.SortGiftCertificateService;
import org.springframework.lang.Nullable;

import java.util.Comparator;
import java.util.List;

public class SortByFields implements SortGiftCertificateService {
    /**
     * Field to sort by.
     */
    private String sortFields;
    /**
     * Order to sort.
     * Value must be "asc" or "desc" to specify sort order, divided by ",".
     */
    private String sortOrder;

    public SortByFields(@Nullable String sortFields, @Nullable String sortOrder) {
        this.sortFields = sortFields;
        this.sortOrder = sortOrder;
    }

    @Override
    public void sort(List<GiftCertificate> certificates) {
        if (sortOrder != null && sortFields != null) {
            Comparator<GiftCertificate> comparator = getComparator();
            certificates.sort(comparator);
        }
    }

    private Comparator<GiftCertificate> getComparator() {
        Comparator<GiftCertificate> comparator = null;


        if (sortOrder != null && sortFields != null) {
            sortFields = sortFields.replace(" ", "");
            sortOrder = sortOrder.replace(" ", "");
            String[] fields = sortFields.split(",");
            String[] orderAscDesc = sortOrder.split(",");

            for (int i = 0; i < fields.length; ++i) {
                if (fields[i].equalsIgnoreCase("name")) {
                    comparator = addByNameComparison(comparator, orderAscDesc[i]);
                }
                if (fields[i].equalsIgnoreCase("createDate")) {
                    comparator = addByDateComparison(comparator, orderAscDesc[i]);
                }
                if (fields[i].equalsIgnoreCase("id")) {
                    comparator = addByIdComparison(comparator, orderAscDesc[i]);
                }
                if (fields[i].equalsIgnoreCase("duration")) {
                    comparator = addByDurationComparison(comparator, orderAscDesc[i]);
                }
                if (fields[i].equalsIgnoreCase("price")) {
                    comparator = addByPriceComparison(comparator, orderAscDesc[i]);
                }
            }
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addByNameComparison(Comparator<GiftCertificate> comparator, String order) {
        if (comparator == null) {
            comparator = isDescendingOrder(order)
                    ? Comparator.comparing(GiftCertificate::getName).reversed()
                    : Comparator.comparing(GiftCertificate::getName);

        } else {
            comparator = isDescendingOrder(order)
                    ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getName).reversed())
                    : comparator.thenComparing(GiftCertificate::getName);
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addByDateComparison(Comparator<GiftCertificate> comparator, String order) {
        if (comparator == null) {
            comparator = isDescendingOrder(order)
                    ? Comparator.comparing(GiftCertificate::getCreateDate).reversed()
                    : Comparator.comparing(GiftCertificate::getCreateDate);

        } else {
            comparator = isDescendingOrder(order)
                    ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getCreateDate).reversed())
                    : comparator.thenComparing(GiftCertificate::getCreateDate);
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addByIdComparison(Comparator<GiftCertificate> comparator, String order) {
        if (comparator == null) {
            comparator = isDescendingOrder(order)
                    ? Comparator.comparing(GiftCertificate::getId).reversed()
                    : Comparator.comparing(GiftCertificate::getId);

        } else {
            comparator = isDescendingOrder(order)
                    ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getId).reversed())
                    : comparator.thenComparing(GiftCertificate::getId);
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addByDurationComparison(Comparator<GiftCertificate> comparator, String order) {
        if (comparator == null) {
            comparator = isDescendingOrder(order)
                    ? Comparator.comparing(GiftCertificate::getDuration).reversed()
                    : Comparator.comparing(GiftCertificate::getDuration);

        } else {
            comparator = isDescendingOrder(order)
                    ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getDuration).reversed())
                    : comparator.thenComparing(GiftCertificate::getDuration);
        }
        return comparator;
    }

    private Comparator<GiftCertificate> addByPriceComparison(Comparator<GiftCertificate> comparator, String order) {
        if (comparator == null) {
            comparator = isDescendingOrder(order)
                    ? Comparator.comparing(GiftCertificate::getPrice).reversed()
                    : Comparator.comparing(GiftCertificate::getPrice);

        } else {
            comparator = isDescendingOrder(order)
                    ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getPrice).reversed())
                    : comparator.thenComparing(GiftCertificate::getPrice);
        }
        return comparator;
    }

    private boolean isDescendingOrder(String order) {
        return order.equalsIgnoreCase("desc");
    }
}