package com.opensource.api.order.service.impl;

import com.opensource.api.order.service.api.OrderProcessingService;
import com.opensource.api.order.service.dao.OrderProcessingDao;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by amg871 on 5/20/17.
 */
@Named
public class OrderProcessingServiceImpl implements OrderProcessingService{

    @Inject
    private OrderProcessingDao orderProcessingDao;

    @Override
    public void processOrderRequest() {
        orderProcessingDao.processOrderRequestFromTopic();
    }
}
