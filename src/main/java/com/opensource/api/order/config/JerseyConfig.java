package com.opensource.api.order.config;

import com.opensource.api.order.rest.OrderIntakeResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(OrderIntakeResource.class);
    }

}

