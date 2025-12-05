package com.care.hub.services;

import com.care.hub.data.entities.Nurse;
import com.care.hub.data.repositories.NurseJdbcRepository;
import com.carehub.nurses.model.CreateNurseDTO;
import com.carehub.nurses.model.NurseDTO;
import com.carehub.nurses.model.PaginatedNursesDTO;
import com.carehub.nurses.model.UpdateNurseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class NurseService {

    @Autowired
    private NurseJdbcRepository nurseRepository;

    public NurseDTO createNurse(CreateNurseDTO body) {
        var entity = new Nurse()
                .setName(body.getName())
                .setLogin(body.getLogin())
                .setPassword(body.getPassword())
                .setCpf(body.getCpf())
                .setCoren(body.getCoren());

        var saved = nurseRepository.save(entity);
        return toDTO(saved);
    }

    public NurseDTO findById(Long nurseId) {
        var entity = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse not found"));
        return toDTO(entity);
    }

    public PaginatedNursesDTO listNurses(Integer page, Integer perPage) {
        int p = page != null ? page : 0;
        int pp = perPage != null ? perPage : 10;

        var items = nurseRepository.findAll(p, pp);
        var total = nurseRepository.count();

        var dto = new PaginatedNursesDTO();
        dto.setPage(p);
        dto.setPerPage(pp);
        dto.setTotal((long) total);
        dto.setItems(items.stream().map(this::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public NurseDTO updateNurse(Long nurseId, UpdateNurseDTO body) {
        var entity = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse not found"));

        if (body.getName() != null) entity.setName(body.getName());
        if (body.getCpf() != null) entity.setCpf(body.getCpf());
        if (body.getCoren() != null) entity.setCoren(body.getCoren());

        nurseRepository.update(entity);
        return toDTO(entity);
    }

    public void deleteNurse(Long nurseId) {
        nurseRepository.deleteById(nurseId);
    }

    private NurseDTO toDTO(Nurse entity) {
        var dto = new NurseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLogin(entity.getLogin());
        dto.setCpf(entity.getCpf());
        dto.setCoren(entity.getCoren());
        return dto;
    }
}
