package com.epam.esm.facade;

import com.epam.esm.dao.DaoException;
import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.Metadata;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

public interface OrderFacade {
    @NonNull
    JsonResult<Order> getOrder(int id);

    @NonNull
    JsonResult<Order> save(Order order);

    @NonNull
    JsonResult<Order> delete(int id);

    @NonNull
    JsonResult<Order> getAllOrders(int page, int perPage, boolean includeMetadata);

    @NonNull
    JsonResult<Order> search(int userId, int page, int perPage, boolean includeMetadata);

    @NonNull
    JsonResult<Order> getOrder(int id, Set<String> fieldsToFind);
}
