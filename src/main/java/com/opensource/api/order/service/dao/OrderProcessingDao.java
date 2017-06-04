package com.opensource.api.order.service.dao;

import com.opensource.api.order.model.OrderRequest;

import java.util.List;

/**
 * Created by amg871 on 5/20/17.
 */
public interface OrderProcessingDao {
    public void processOrderRequestFromTopic();
}
