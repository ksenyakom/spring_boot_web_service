package com.epam.esm.facade;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrderFacade {
    @NonNull
    JsonResult<Order> getOrder(int id);

    @NonNull
    JsonResult<Order> save(Order order);

    @NonNull
    JsonResult<Order> delete(int id);

     @NonNull
    JsonResult<Order> getAllOrders();

}
