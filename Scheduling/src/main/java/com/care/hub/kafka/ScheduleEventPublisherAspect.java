package com.care.hub.kafka;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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
    public void afterScheduleSaved(Object result) {
        try {
            Map<String, Object> scheduleData = extractScheduleData(result);

            Map<String, Object> payload = new HashMap<>();
            payload.put("eventType", "SCHEDULE_CREATED");
            payload.put("schedule", scheduleData);

            String key = Optional.ofNullable(scheduleData.get("id")).map(Object::toString).orElse(null);
            kafkaTemplate.send(TOPIC, key, payload);
            log.info("Evento SCHEDULE_CREATED publicado no tópico {} com chave {} e payload {}", TOPIC, key, payload);
        } catch (Exception e) {
            log.error("Falha ao publicar evento SCHEDULE_CREATED no Kafka", e);
        }
    }

    private Map<String, Object> extractScheduleData(Object entity) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", tryGetter(entity, "getId"));
        m.put("patientId", tryGetter(entity, "getPatientId"));
        m.put("doctorId", tryGetter(entity, "getDoctorId"));
        m.put("nurseId", tryGetter(entity, "getNurseId"));
        m.put("date", tryGetter(entity, "getDate"));
        m.put("dateTime", tryGetter(entity, "getDateTime"));
        m.put("appointmentDate", tryGetter(entity, "getAppointmentDate"));
        m.put("startTime", tryGetter(entity, "getStartTime"));
        m.put("endTime", tryGetter(entity, "getEndTime"));
        m.put("status", tryGetter(entity, "getStatus"));
        m.put("notes", tryGetter(entity, "getNotes"));
        return m;
    }

    private Object tryGetter(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            return m.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }
}
