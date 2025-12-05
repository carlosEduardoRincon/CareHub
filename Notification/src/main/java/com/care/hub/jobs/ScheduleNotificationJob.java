package com.care.hub.jobs;

import com.care.hub.data.NotificationScheduleRepository;
import com.care.hub.email.EmailService;
import com.care.hub.model.ScheduleNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "notification.job", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ScheduleNotificationJob {

    private static final Logger log = LoggerFactory.getLogger(ScheduleNotificationJob.class);

    private final NotificationScheduleRepository repository;
    private final EmailService emailService;
    private final int windowMinutes;

    public ScheduleNotificationJob(NotificationScheduleRepository repository,
                                   EmailService emailService,
                                   org.springframework.core.env.Environment env) {
        this.repository = repository;
        this.emailService = emailService;
        this.windowMinutes = Integer.parseInt(env.getProperty("notification.job.upcoming-window-minutes", "60"));
    }

    @Scheduled(fixedDelayString = "${notification.job.fixed-delay-ms:300000}")
    public void processUpcomingSchedules() {
        var today = LocalDate.now();
        var now = LocalTime.now();
        var end = now.plusMinutes(windowMinutes);

        var items = repository.findUpcomingWithin(today, now, end);
        if (items.isEmpty()) {
            log.debug("Nenhum agendamento encontrado para enviar e-mails no intervalo de {} minutos.", windowMinutes);
            return;
        }
        log.info("Enviando e-mails para {} agendamento(s) nas próximas {} minuto(s).", items.size(), windowMinutes);

        items.forEach(emailService::sendAppointmentEmails);
    }
}
