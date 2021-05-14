package com.epam.esm.service.search;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;

import java.util.List;

public interface SearchOrderService {
    List<Order> search(OrderService service);
    int getTotalFound();
}
