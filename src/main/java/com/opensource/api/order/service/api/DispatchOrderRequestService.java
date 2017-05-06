package com.opensource.api.order.service.api;

import com.opensource.api.order.model.OrderRequest;

/**
 * Created by amg871 on 5/6/17.
 */
public interface DispatchOrderRequestService {
    public void sendOrderForProcessing(OrderRequest orderRequest);
}
