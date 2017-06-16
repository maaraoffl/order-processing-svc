package com.opensource.api.order.service.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensource.api.order.model.Order;
import com.opensource.api.order.model.OrderRequest;
import com.opensource.api.order.service.dao.OrderProcessingDao;
import com.opensource.api.order.service.util.ZookeeperUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by amg871 on 5/20/17.
 */

@Named
public class OrderProcessingDaoImpl implements OrderProcessingDao{

//    @Value("${order.process.topic.brokers}")
    private String orderProcessTopicBrokers;

    @Value("${order.process.topic.name}")
    private String orderProcessTopicName;

    @Value("${order.process.consumer.group}")
    private String orderProcessConsumerGroup;

//    @Value("${order.delivery.topic.brokers}")
    private String orderDeliveryTopicBrokers;

    @Value("${order.delivery.topic.name}")
    private String orderDeliveryTopicName;

    @Value("${zookeeper.address}")
    private String zooKeeperAddress;

    @Inject
    private ZookeeperUtil zookeeperUtil;

    @PostConstruct
    public void postConstruct() throws Exception{
        String brokers=zookeeperUtil.getBrokers(zooKeeperAddress);
        orderProcessTopicBrokers=brokers;
        orderDeliveryTopicBrokers=brokers;
    }

    @Override
    public void processOrderRequestFromTopic() {
        Properties props = new Properties();
        props.put("bootstrap.servers", orderProcessTopicBrokers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", orderProcessConsumerGroup);

        KafkaConsumer consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList(orderProcessTopicName));

        try {
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                List<OrderRequest> orderRequestList = new ArrayList<OrderRequest>();
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(
                            (String.format("Topic %s, Partition %d, Offset %d, Key %s, Value %s",
                                    record.topic(), record.partition(), record.offset(), record.key(), record.value())));
                    ObjectMapper mapper = new ObjectMapper();
                    OrderRequest request = mapper.readValue(record.value(), OrderRequest.class);
                    orderRequestList.add(request);
                }
                triggerProcessing(orderRequestList);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            consumer.close();
        }
    }
    protected void triggerProcessing(final List<OrderRequest> orderRequestList)
    {
        orderRequestList.forEach(orderRequest -> {
            try {
                Thread.sleep(100);
                Order order = new Order();
                order.setId(orderRequest.getId());
                order.setName(orderRequest.getName());
                order.setCount(orderRequest.getCount());
                order.setStatus("COMPLETED");
                sendRequestToProcessQueue(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendRequestToProcessQueue(Order order) {
        Properties props = new Properties();
        props.put("bootstrap.servers", orderDeliveryTopicBrokers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(props);
        try{

            ObjectMapper mapper = new ObjectMapper();
            String orderAsJsonString = mapper.writeValueAsString(order);
            String key = String.valueOf(Instant.now().toEpochMilli());

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(orderDeliveryTopicName, key, orderAsJsonString);
            myProducer.send(record, (metadata, exception) -> {
                if(exception !=null)
                    System.out.println("Message not delivered: " + exception.getMessage());
                else
                    System.out.println("Message delivered: " + metadata.topic() + ":" + metadata.offset());

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            myProducer.close();
        }

    }
}
