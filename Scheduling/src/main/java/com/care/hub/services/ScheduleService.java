package com.care.hub.services;

import com.care.hub.data.entities.Schedule;
import com.care.hub.data.repositories.ScheduleConflictRepository;
import com.care.hub.data.repositories.ScheduleJdbcRepository;
import com.carehub.schedules.model.CreateScheduleDTO;
import com.carehub.schedules.model.PaginatedSchedulesDTO;
import com.carehub.schedules.model.ScheduleDTO;
import com.carehub.schedules.model.UpdateScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleJdbcRepository scheduleRepository;

    @Autowired
    private ScheduleConflictRepository scheduleConflictRepository;

    public ScheduleDTO createSchedule(CreateScheduleDTO body) {
        var dateTime = body.getScheduleDate();
        var date = dateTime.toLocalDate();
        var time = dateTime.toLocalTime();
        var doctorId = body.getDoctorId();

        if (scheduleConflictRepository.existsByDoctorAndDateTime(doctorId, date, time)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already an appointment scheduled with this doctor at this time.");
        }

        var entity = new Schedule()
                .setDoctorId(doctorId)
                .setPatientId(body.getPatientId())
                .setScheduleDate(date)
                .setScheduleHour(time)
                .setObservation(body.getObservation())
                .setStatus("SCHEDULED");

        var saved = scheduleRepository.save(entity);
        return toDTO(saved);
    }

    public ScheduleDTO findById(Long scheduleId) {
        var entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
        return toDTO(entity);
    }

    public PaginatedSchedulesDTO listSchedules(Integer page, Integer perPage) {
        int p = page != null ? page : 0;
        int pp = perPage != null ? perPage : 10;

        var items = scheduleRepository.findAll(p, pp);
        var total = scheduleRepository.count();

        var dto = new PaginatedSchedulesDTO();
        dto.setPage(p);
        dto.setPerPage(pp);
        dto.setTotal((long) total);
        dto.setItems(items.stream().map(this::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public ScheduleDTO updateSchedule(Long scheduleId, UpdateScheduleDTO body) {
        var entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        if (body.getScheduleDate() != null) entity.setScheduleDate(body.getScheduleDate().toLocalDate());
        if (body.getObservation() != null) entity.setObservation(body.getObservation());
        if (body.getStatus() != null) entity.setStatus(body.getStatus());

        scheduleRepository.update(entity);
        return toDTO(entity);
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    private ScheduleDTO toDTO(Schedule entity) {
        var scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(entity.getId());
        scheduleDTO.setDoctorId(entity.getDoctorId());
        scheduleDTO.setPatientId(entity.getPatientId());
        if (entity.getScheduleDate() != null) {
            var time = entity.getScheduleHour() != null ? entity.getScheduleHour() : java.time.LocalTime.MIDNIGHT;
            scheduleDTO.setScheduleDate(java.time.OffsetDateTime.of(entity.getScheduleDate(), time, java.time.ZoneOffset.UTC));
        } else {
            scheduleDTO.setScheduleDate(null);
        }
        scheduleDTO.setObservation(entity.getObservation());
        scheduleDTO.setStatus(entity.getStatus());

        return scheduleDTO;
    }
}