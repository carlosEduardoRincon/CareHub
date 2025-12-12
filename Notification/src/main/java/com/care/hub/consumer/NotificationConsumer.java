package com.care.hub.consumer;

import com.care.hub.data.repositories.NotificationScheduleRepository;
import com.care.hub.services.EmailService;
import com.care.hub.data.entities.ScheduleNotification;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final EmailService emailService;
    private final NotificationScheduleRepository scheduleRepository;

    public NotificationConsumer(EmailService emailService,
                                NotificationScheduleRepository scheduleRepository) {
        this.emailService = emailService;
        this.scheduleRepository = scheduleRepository;
    }

    @KafkaListener(
            topics = "notification-care-hub",
            groupId = "${spring.kafka.consumer.group-id:notification-service}"
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

            var scheduleObj = m.get("schedule");
            var scheduleId = extractScheduleId(scheduleObj);
            if (scheduleId == null) {
                log.warn("ScheduleId missing from payload\n: {}", scheduleObj);
                return;
            }

            var sn = scheduleRepository.findByScheduleId(scheduleId);
            sn.ifPresentOrElse(
                    emailService::sendAppointmentEmails,
                    () -> log.warn("Appointment ID={} not found for email sending.", scheduleId)
            );
        } catch (Exception e) {
            log.error("Error processing Kafka message.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Long extractScheduleId(Object scheduleObj) {
        if (scheduleObj instanceof Map<?, ?> sm) {
            var id = sm.get("nr_seq_schedule");
            if (id != null) {
                try {
                    return Long.valueOf(id.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }
}