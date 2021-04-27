package com.epam.esm.service.sort.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.sort.SortGiftCertificateService;
import org.springframework.lang.Nullable;

import java.util.Comparator;
import java.util.List;

public class SortByNameAndDate implements SortGiftCertificateService {
    /**
     * if value != null, then sorting by name performed.
     * Value must be "asc" or "desc" to specify sort order.
     */
    private final String sortByName;
    /**
     * if value != null, then sorting by date created performed.
     * Value must be "asc" or "desc" to specify sort order.
     */
    private final String sortByDate;

    public SortByNameAndDate(@Nullable String sortByName, @Nullable String sortByDate) {
        this.sortByName = sortByName;
        this.sortByDate = sortByDate;
    }

    @Override
    public void sort(List<GiftCertificate> certificates) {
        if (sortByDate != null || sortByName != null) {
            Comparator<GiftCertificate> comparator = getComparator();
            certificates.sort(comparator);
        }
    }

    private Comparator<GiftCertificate> getComparator() {
        Comparator<GiftCertificate> comparator = addSortByNameComparison();
        comparator = addSortByDateComparison(comparator);
        return comparator;
    }

    private Comparator<GiftCertificate> addSortByNameComparison() {
        if (sortByName != null) {
            return isDescendingOrder(sortByName)
                    ? Comparator.comparing(GiftCertificate::getName).reversed()
                    : Comparator.comparing(GiftCertificate::getName);
        }
        return null;
    }

    private Comparator<GiftCertificate> addSortByDateComparison(Comparator<GiftCertificate> comparator) {
        if (sortByDate != null) {
            if (comparator == null) {
                comparator = isDescendingOrder(sortByDate)
                        ? Comparator.comparing(GiftCertificate::getCreateDate).reversed()
                        : Comparator.comparing(GiftCertificate::getCreateDate);

            } else {
                comparator = isDescendingOrder(sortByDate)
                        ? comparator.thenComparing(Comparator.comparing(GiftCertificate::getCreateDate).reversed())
                        : comparator.thenComparing(GiftCertificate::getCreateDate);
            }
        }
        return comparator;
    }

    private boolean isDescendingOrder(String order) {
        return order.equalsIgnoreCase("desc");
    }
}