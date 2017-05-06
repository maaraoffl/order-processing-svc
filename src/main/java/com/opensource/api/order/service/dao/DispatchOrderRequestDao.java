package com.opensource.api.order.service.dao;

import com.opensource.api.order.model.OrderRequest;
import org.springframework.stereotype.Component;

/**
 * Created by amg871 on 5/6/17.
 */

public interface DispatchOrderRequestDao {
    public void sendRequestToProcessQueue(OrderRequest orderRequest);

}
