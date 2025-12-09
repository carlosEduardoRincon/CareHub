package com.care.hub.services;

import com.care.hub.data.entities.Patient;
import com.care.hub.data.repositories.PatientJdbcRepository;
import com.care.hub.data.repositories.UserJdbcRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientJdbcRepository patientRepository;

    @Mock
    private UserJdbcRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientService service;

    @Test
    void findById_returnsPatientDTO_whenFound() {
        var patient = new Patient()
                .setId(9L)
                .setName("John")
                .setEmail("john@doe.com")
                .setAddress("Street 1")
                .setTelephone("9999999")
                .setUserId(100L);

        when(patientRepository.findById(9L)).thenReturn(Optional.of(patient));

        var dto = service.findById(9L);

        assertNotNull(dto);
        assertEquals(9L, dto.getId());
        assertEquals("John", dto.getName());
        verify(patientRepository).findById(9L);
    }

    @Test
    void findById_throwsNotFound_whenMissing() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class, () -> service.findById(404L));
        assertEquals(404, ex.getStatusCode().value());
        verify(patientRepository).findById(404L);
    }

    @Test
    void listPatients_mapsItems_andUsesProvidedPagination() {
        var p1 = new Patient()
                .setId(1L)
                .setName("A")
                .setEmail("a@a.com")
                .setUserId(1L);
        when(patientRepository.findAll(1, 5)).thenReturn(List.of(p1));
        when(patientRepository.count()).thenReturn(1);

        var page = service.listPatients(1, 5);

        assertNotNull(page);
        assertEquals(1, page.getPage());
        assertEquals(5, page.getPerPage());
        assertEquals(1L, page.getTotal());
        assertEquals(1, page.getItems().size());
        verify(patientRepository).findAll(1, 5);
        verify(patientRepository).count();
    }

    @Test
    void deletePatient_callsRepository() {
        service.deletePatient(321L);
        verify(patientRepository).deleteById(321L);
    }
}
