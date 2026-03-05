package com.dtt.organization.util;

import com.dtt.organization.request.entity.LogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    Logger logger= LoggerFactory.getLogger(KafkaSender.class);
    /** Primary topic */
    @Value("${com.dt.kafka.topic.central}")
    private String topic;

    /** RA topic (second send) */
    @Value("${com.dt.kafka.topic.ra}")
    private String topicRA;

    public void send(LogModel logmodel) {

        kafkaTemplate.send(topic, logmodel);
        kafkaTemplate.send(topicRA, logmodel);
        logger.info("Kafka messages sent to topics: {}, {}", topic, topic);
        logger.info("Kafka messages sent to topics: {}, {}", topic, topicRA);
    }
}
