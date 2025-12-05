package com.care.hub.notification;

import com.care.hub.data.NotificationScheduleRepository;
import com.care.hub.email.EmailService;
import com.care.hub.model.ScheduleNotification;
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
        log.info("Mensagem recebida no Notification: topic={}, key={}, value={}", record.topic(), key, record.value());

        try {
            if (!(record.value() instanceof Map<?, ?> m)) {
                log.warn("Payload inesperado: {}", record.value());
                return;
            }
            Object eventType = m.get("eventType");
            if (eventType == null || !"SCHEDULE_CREATED".equals(eventType.toString())) {
                log.debug("Evento ignorado: {}", eventType);
                return;
            }

            Object scheduleObj = m.get("schedule");
            Long scheduleId = extractScheduleId(scheduleObj);
            if (scheduleId == null) {
                log.warn("ScheduleId ausente no payload: {}", scheduleObj);
                return;
            }

            Optional<ScheduleNotification> sn = scheduleRepository.findByScheduleId(scheduleId);
            sn.ifPresentOrElse(
                    emailService::sendAppointmentEmails,
                    () -> log.warn("Agendamento id={} não encontrado para envio de e-mail.", scheduleId)
            );
        } catch (Exception e) {
            log.error("Erro ao processar mensagem Kafka", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Long extractScheduleId(Object scheduleObj) {
        if (scheduleObj instanceof Map<?, ?> sm) {
            Object id = sm.get("id");
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