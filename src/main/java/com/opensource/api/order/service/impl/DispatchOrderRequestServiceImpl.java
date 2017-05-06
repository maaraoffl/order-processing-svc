package com.opensource.api.order.service.impl;

import com.opensource.api.order.model.OrderRequest;
import com.opensource.api.order.service.api.DispatchOrderRequestService;
import com.opensource.api.order.service.dao.DispatchOrderRequestDao;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by amg871 on 5/6/17.
 */

@Component
public class DispatchOrderRequestServiceImpl implements DispatchOrderRequestService{

    @Inject
    private DispatchOrderRequestDao dispatchOrderRequestDao;

    @Override
    public void sendOrderForProcessing(OrderRequest orderRequest) {
        dispatchOrderRequestDao.sendRequestToProcessQueue(orderRequest);
    }
}
