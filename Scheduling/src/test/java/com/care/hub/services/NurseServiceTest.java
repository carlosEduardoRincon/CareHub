package com.care.hub.services;

import com.care.hub.data.entities.Nurse;
import com.care.hub.data.repositories.NurseJdbcRepository;
import com.care.hub.data.repositories.UserJdbcRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NurseServiceTest {

    @Mock
    private NurseJdbcRepository nurseRepository;

    @Mock
    private UserJdbcRepository userRepository;

    @InjectMocks
    private NurseService service;

    @Test
    void findById_returnsNurseDTO_whenFound() {
        var entity = new Nurse()
                .setId(2L)
                .setName("Clara")
                .setCoren("COREN123");

        when(nurseRepository.findById(2L)).thenReturn(Optional.of(entity));

        var dto = service.findById(2L);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Clara", dto.getName());
        verify(nurseRepository).findById(2L);
    }

    @Test
    void findById_throwsNotFound_whenMissing() {
        when(nurseRepository.findById(anyLong())).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class, () -> service.findById(404L));
        assertEquals(404, ex.getStatusCode().value());
        verify(nurseRepository).findById(404L);
    }

    @Test
    void listNurses_usesDefaultPaginationAndMapsItems() {
        var entity = new Nurse()
                .setId(10L)
                .setName("Amy")
                .setCoren("COREN999");

        when(nurseRepository.findAll(0, 10)).thenReturn(List.of(entity));
        when(nurseRepository.count()).thenReturn(1);

        var page = service.listNurses(null, null);

        assertNotNull(page);
        assertEquals(0, page.getPage());
        assertEquals(10, page.getPerPage());
        assertEquals(1L, page.getTotal());
        assertEquals(1, page.getItems().size());
        verify(nurseRepository).findAll(0, 10);
        verify(nurseRepository).count();
    }

    @Test
    void deleteNurse_callsRepository() {
        service.deleteNurse(77L);
        verify(nurseRepository).deleteById(77L);
    }
}
