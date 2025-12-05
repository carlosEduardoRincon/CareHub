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

    public void sendAppointmentEmails(ScheduleNotification sn) {
        var subjectPatient = "Confirmação de agendamento | CareHub";
        var bodyPatient = String.format(
                "Olá %s,\n\nSeu agendamento foi confirmado.\n" +
                "Médico(a): %s\n" +
                "Data: %s\nHora: %s\n" +
                "Observações: %s\n\n" +
                "Equipe CareHub.",
                safe(sn.getPatientName()), safe(sn.getDoctorName()),
                sn.getDate(), sn.getTime(),
                safe(sn.getObservation())
        );
        sendSimple(sn.getPatientEmail(), subjectPatient, bodyPatient);

        var subjectDoctor = "Novo agendamento | CareHub";
        var bodyDoctor = String.format(
                "Olá %s,\n\nVocê possui um novo agendamento.\n" +
                "Paciente: %s\n" +
                "Data: %s\nHora: %s\n" +
                "Observações: %s\n\n" +
                "Equipe CareHub.",
                safe(sn.getDoctorName()), safe(sn.getPatientName()),
                sn.getDate(), sn.getTime(),
                safe(sn.getObservation())
        );
        sendSimple(sn.getDoctorEmail(), subjectDoctor, bodyDoctor);
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
