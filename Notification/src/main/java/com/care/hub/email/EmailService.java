package com.care.hub.email;

import com.care.hub.model.ScheduleNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from:no-reply@carehub.local}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentEmails(ScheduleNotification scheduleNotification) {
        var subjectPatient = "Confirmação de agendamento | CareHub";
        var bodyPatient = String.format(
                "Olá %s,\n\nSeu agendamento foi confirmado.\n" +
                "Médico(a): %s\n" +
                "Data: %s\n" +
                "Observações: %s\n\n" +
                "Equipe CareHub.",
                safe(scheduleNotification.getPatientName()), safe(scheduleNotification.getDoctorName()),
                scheduleNotification.getDate(),
                safe(scheduleNotification.getObservation())
        );
        sendSimple(scheduleNotification.getPatientEmail(), subjectPatient, bodyPatient);

        var subjectDoctor = "Novo agendamento | CareHub";
        var bodyDoctor = String.format(
                "Olá %s,\n\nVocê possui um novo agendamento.\n" +
                "Paciente: %s\n" +
                "Data: %s\n" +
                "Observações: %s\n\n" +
                "Equipe CareHub.",
                safe(scheduleNotification.getDoctorName()), safe(scheduleNotification.getPatientName()),
                scheduleNotification.getDate(),
                safe(scheduleNotification.getObservation())
        );
        sendSimple(scheduleNotification.getDoctorEmail(), subjectDoctor, bodyDoctor);
    }

    public void sendDailyEmails(ScheduleNotification scheduleNotification) {
        var subjectPatient = "Lembre-se da sua consulta de hoje | CareHub";
        var bodyPatient = String.format(
                "Olá %s,\n\nSeu agendamento será hoje.\n" +
                        "Médico(a): %s\n" +
                        "Data: %s\n" +
                        "Observações: %s\n\n" +
                        "Equipe CareHub.",
                safe(scheduleNotification.getPatientName()), safe(scheduleNotification.getDoctorName()),
                scheduleNotification.getDate(),
                safe(scheduleNotification.getObservation())
        );
        sendSimple(scheduleNotification.getPatientEmail(), subjectPatient, bodyPatient);

        var subjectDoctor = "Lembrete consulta %s | CareHub";
        var bodyDoctor = String.format(
                "Olá %s,\n\nVocê possui uma consulta marcada para hoje.\n" +
                        "Paciente: %s\n" +
                        "Data: %s\n" +
                        "Observações: %s\n\n" +
                        "Equipe CareHub.",
                safe(scheduleNotification.getPatientName()),
                safe(scheduleNotification.getDoctorName()), safe(scheduleNotification.getPatientName()),
                scheduleNotification.getDate(),
                safe(scheduleNotification.getObservation())
        );
        sendSimple(scheduleNotification.getDoctorEmail(), subjectDoctor, bodyDoctor);
    }

    private void sendSimple(String to, String subject, String text) {
        if (to == null || to.isBlank()) return;
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String safe(String v) {
        return v == null ? "" : v;
    }
}
