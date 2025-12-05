package com.care.hub.services;

import com.carehub.schedules.model.CreateScheduleDTO;
import com.carehub.schedules.model.ScheduleDTO;
import com.carehub.schedules.model.PaginatedSchedulesDTO;
import com.carehub.schedules.model.UpdateScheduleDTO;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    public ScheduleDTO createSchedule(CreateScheduleDTO body) {
        return null;
    }

    public ScheduleDTO findById(Long scheduleId) {
        return null;
    }

    public PaginatedSchedulesDTO listSchedules(Integer page, Integer perPage) {
        return null;
    }

    public ScheduleDTO updateSchedule(Long scheduleId, UpdateScheduleDTO body) {
        return null;
    }

    public void deleteSchedule(Long scheduleId) {
    }
}