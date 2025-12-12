package com.care.hub.jobs;

import com.care.hub.data.repositories.NotificationScheduleRepository;
import com.care.hub.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(prefix = "notification.job", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ScheduleNotificationJob {

    private static final Logger log = LoggerFactory.getLogger(ScheduleNotificationJob.class);

    private final NotificationScheduleRepository repository;
    private final EmailService emailService;

    public ScheduleNotificationJob(NotificationScheduleRepository repository,
                                   EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelayString = "${notification.job.fixed-delay-ms}")
    public void processUpcomingSchedules() {
        var today = LocalDate.now();

        var items = repository.findUpcomingWithin(today);
        if (items.isEmpty()) {
            log.debug("No scheduled emails were found to be sent");
            return;
        }
        log.info("Sending emails to {} scheduling(s).", items.size());

        items.forEach(emailService::sendDailyEmails);
    }
}
