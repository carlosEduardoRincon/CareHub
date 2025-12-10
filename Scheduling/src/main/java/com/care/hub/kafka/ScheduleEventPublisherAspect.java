package com.care.hub.kafka;

import com.care.hub.data.entities.Schedule;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class ScheduleEventPublisherAspect {

    private static final Logger log = LoggerFactory.getLogger(ScheduleEventPublisherAspect.class);
    private static final String TOPIC = "notification-care-hub";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ScheduleEventPublisherAspect(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @AfterReturning(
            pointcut = "execution(* com.care.hub.data.repositories..*Schedule*Repository.save(..))",
            returning = "result"
    )
    public void afterScheduleSaved(Schedule result) {
        try {
            Map<String, Object> scheduleData = extractScheduleData(result);

            Map<String, Object> payload = new HashMap<>();
            payload.put("eventType", "SCHEDULE_CREATED");
            payload.put("schedule", scheduleData);

            String key = Optional.ofNullable(scheduleData.get("id")).map(Object::toString).orElse(null);
            kafkaTemplate.send(TOPIC, key, payload);
            log.info("Event SCHEDULE_CREATED posted in topic {} with key {} and payload {}", TOPIC, key, payload);
        } catch (Exception e) {
            log.error("Failed to publish SCHEDULE_CREATED event in Kafka.", e);
        }
    }

    private Map<String, Object> extractScheduleData(Schedule schedule) {
        Map<String, Object> scheduleMessageHashMap = new HashMap<>();

        scheduleMessageHashMap.put("nr_seq_schedule", schedule.getId());
        scheduleMessageHashMap.put("nr_seq_patient", schedule.getPatientId());
        scheduleMessageHashMap.put("nr_seq_doctor", schedule.getDoctorId());
        scheduleMessageHashMap.put("schedule_date", schedule.getScheduleDate());
        scheduleMessageHashMap.put("status", schedule.getStatus());
        scheduleMessageHashMap.put("observation", schedule.getObservation());

        return scheduleMessageHashMap;
    }
}
