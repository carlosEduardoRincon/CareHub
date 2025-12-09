package com.care.hub.services;

import com.care.hub.data.entities.Schedule;
import com.care.hub.data.repositories.ScheduleConflictRepository;
import com.care.hub.data.repositories.ScheduleJdbcRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleJdbcRepository scheduleRepository;

    @Mock
    private ScheduleConflictRepository scheduleConflictRepository;

    @InjectMocks
    private ScheduleService service;

    @Test
    void findById_returnsScheduleDTO_whenFound() {
        var schedule = mock(Schedule.class);
        when(schedule.getId()).thenReturn(1L);
        when(schedule.getDoctorId()).thenReturn(2L);
        when(schedule.getPatientId()).thenReturn(3L);
        when(schedule.getScheduleDate()).thenReturn(OffsetDateTime.now().toLocalDate());
        when(schedule.getObservation()).thenReturn("observação");
        when(schedule.getStatus()).thenReturn(null);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        var dto = service.findById(1L);

        assertNotNull(dto);
        verify(scheduleRepository).findById(1L);
    }

    @Test
    void findById_throwsNotFound_whenMissing() {
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class, () -> service.findById(999L));
        assertEquals(404, ex.getStatusCode().value());
        verify(scheduleRepository).findById(999L);
    }

    @Test
    void listSchedules_usesDefaultPaginationAndMapsItems() {
        var schedule = mock(Schedule.class);
        when(schedule.getId()).thenReturn(11L);
        when(schedule.getDoctorId()).thenReturn(22L);
        when(schedule.getPatientId()).thenReturn(33L);
        when(schedule.getScheduleDate()).thenReturn(OffsetDateTime.now().toLocalDate());
        when(schedule.getObservation()).thenReturn("ok");
        when(schedule.getStatus()).thenReturn(null);

        when(scheduleRepository.findAll(0, 10)).thenReturn(List.of(schedule));
        when(scheduleRepository.count()).thenReturn(1);

        var page = service.listSchedules(null, null);

        assertNotNull(page);
        assertEquals(0, page.getPage());
        assertEquals(10, page.getPerPage());
        assertEquals(1L, page.getTotal());
        assertEquals(1, page.getItems().size());
        verify(scheduleRepository).findAll(0, 10);
        verify(scheduleRepository).count();
    }

    @Test
    void deleteSchedule_callsRepository() {
        service.deleteSchedule(123L);
        verify(scheduleRepository).deleteById(123L);
    }
}
