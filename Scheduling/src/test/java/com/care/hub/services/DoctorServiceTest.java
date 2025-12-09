package com.care.hub.services;

import com.care.hub.data.entities.Doctor;
import com.care.hub.data.repositories.DoctorJdbcRepository;
import com.care.hub.data.repositories.UserJdbcRepository;
import com.carehub.doctors.model.UpdateDoctorDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorJdbcRepository doctorRepository;

    @Mock
    private UserJdbcRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorService service;

    @Test
    void findById_returnsDoctorDTO_whenFound() {
        var doctor = new Doctor()
                .setId(1L)
                .setName("Dr. House")
                .setEmail("house@hospital.com")
                .setCrm("CRM123")
                .setSpeciality("DIAGNOSTICS")
                .setUserId(10L);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        var dto = service.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Dr. House", dto.getName());
        verify(doctorRepository).findById(1L);
    }

    @Test
    void findById_throwsNotFound_whenMissing() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class, () -> service.findById(99L));
        assertEquals(404, ex.getStatusCode().value());
        verify(doctorRepository).findById(99L);
    }

    @Test
    void listDoctors_usesDefaultPaginationAndMapsItems() {
        var doctor = new Doctor()
                .setId(1L)
                .setName("Dr. Who")
                .setEmail("who@tardis.com")
                .setCrm("CRM001")
                .setSpeciality("TIME")
                .setUserId(5L);

        when(doctorRepository.findAll(0, 10)).thenReturn(List.of(doctor));
        when(doctorRepository.count()).thenReturn(1);

        var page = service.listDoctors(null, null);

        assertNotNull(page);
        assertEquals(0, page.getPage());
        assertEquals(10, page.getPerPage());
        assertEquals(1L, page.getTotal());
        assertEquals(1, page.getItems().size());
        assertEquals(1L, page.getItems().get(0).getId());
        verify(doctorRepository).findAll(0, 10);
        verify(doctorRepository).count();
    }

    @Test
    void updateDoctor_updatesProvidedFields_andCallsRepositoryUpdate() {
        var doctor = new Doctor()
                .setId(7L)
                .setName("Old Name")
                .setEmail("old@mail.com")
                .setCrm("OLDCRM")
                .setSpeciality("OLD_SPEC")
                .setUserId(3L);

        when(doctorRepository.findById(7L)).thenReturn(Optional.of(doctor));

        var body = new UpdateDoctorDTO();
        body.setName("New Name");

        var dto = service.updateDoctor(7L, body);

        assertNotNull(dto);
        assertEquals("New Name", dto.getName());
        assertEquals("old@mail.com", dto.getEmail());
        verify(doctorRepository).findById(7L);
    }

    @Test
    void deleteDoctor_callsRepository() {
        service.deleteDoctor(55L);
        verify(doctorRepository).deleteById(55L);
    }
}
