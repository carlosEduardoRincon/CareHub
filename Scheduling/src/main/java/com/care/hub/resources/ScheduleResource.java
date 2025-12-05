package com.care.hub.resources;

import com.care.hub.services.ScheduleService;
import com.carehub.schedules.api.ScheduleApi;
import com.carehub.schedules.model.CreateScheduleDTO;
import com.carehub.schedules.model.ScheduleDTO;
import com.carehub.schedules.model.PaginatedSchedulesDTO;
import com.carehub.schedules.model.UpdateScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ScheduleResource implements ScheduleApi {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public ResponseEntity<ScheduleDTO> createSchedule(CreateScheduleDTO body) {
        log.info("[POST] - Create Schedule");
        var created = this.scheduleService.createSchedule(body);
        return ResponseEntity
                .created(URI.create("/schedule/" + created.getId()))
                .body(created);
    }

    @Override
    public ResponseEntity<ScheduleDTO> getSchedule(Long scheduleId) {
        log.info("[GET] - Get Schedule with ID: {}", scheduleId);
        var schedule = this.scheduleService.findById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @Override
    public ResponseEntity<PaginatedSchedulesDTO> listSchedules(Integer page, Integer perPage) {
        log.info("[GET] - List Schedules - page: {}, perPage: {}", page, perPage);
        var paginated = this.scheduleService.listSchedules(page, perPage);
        return ResponseEntity.ok(paginated);
    }

    @Override
    public ResponseEntity<ScheduleDTO> updateSchedule(Long scheduleId, UpdateScheduleDTO body) {
        log.info("[PUT] - Update Schedule with ID: {}", scheduleId);
        var updated = this.scheduleService.updateSchedule(scheduleId, body);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteSchedule(Long scheduleId) {
        log.info("[DELETE] - Remove Schedule with ID: {}", scheduleId);
        this.scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
