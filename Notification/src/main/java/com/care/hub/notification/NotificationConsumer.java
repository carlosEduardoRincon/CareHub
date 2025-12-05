package com.care.hub.notification;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(
            topics = "notification-care-hub",
            groupId = "${spring.kafka.consumer.group-id:notification-service}"
    )
    public void onMessage(ConsumerRecord<String, Object> record,
                          @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        log.info("Mensagem recebida no Notification: topic={}, key={}, value={}", record.topic(), key, record.value());
    }
}
