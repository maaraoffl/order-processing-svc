package com.opensource.api.order.main;

import com.opensource.api.order.service.api.OrderProcessingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;

@Configuration
@ComponentScan("com.opensource.api.order")

@EnableAutoConfiguration
public class OrderProcessingApplication extends SpringBootServletInitializer {

    @Inject
    private OrderProcessingService orderProcessingService;

    private static Class<OrderProcessingApplication> applicationClass = OrderProcessingApplication.class;
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(applicationClass, args);
        Arrays.asList(context.getBeanFactory().getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    @PostConstruct
    public void processOrder()
    {
        orderProcessingService.processOrderRequest();
    }
    
}
