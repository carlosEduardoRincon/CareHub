package com.care.hub.consumer;

import com.care.hub.services.HistoryService;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class HistoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(HistoryEventListener.class);

    private final HistoryService historyService;

    public HistoryEventListener(HistoryService historyService) {
        this.historyService = historyService;
    }

    @KafkaListener(
            topics = "notification-care-hub",
            groupId = "${spring.kafka.consumer.group-id:history-service}"
    )
    public void onMessage(ConsumerRecord<String, Object> record,
                          @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        log.info("Message received in Notification: topic={}, key={}, value={}", record.topic(), key, record.value());

        try {
            if (!(record.value() instanceof Map<?, ?> m)) {
                log.warn("Unexpected Payload\n: {}", record.value());
                return;
            }
            var eventType = m.get("eventType");
            if (eventType == null || !"SCHEDULE_CREATED".equals(eventType.toString())) {
                log.debug("Event ignored: {}", eventType);
                return;
            }

            historyService.saveFromMessage(m);

        } catch (Exception e) {
            log.error("Error processing Kafka message.", e);
        }
    }
}