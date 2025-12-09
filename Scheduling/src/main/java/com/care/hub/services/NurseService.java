package com.care.hub.services;

import com.care.hub.data.entities.Nurse;
import com.care.hub.data.entities.User;
import com.care.hub.data.repositories.NurseJdbcRepository;
import com.care.hub.data.repositories.UserJdbcRepository;
import com.care.hub.mappers.NurseMapper;
import com.carehub.nurses.model.CreateNurseDTO;
import com.carehub.nurses.model.NurseDTO;
import com.carehub.nurses.model.PaginatedNursesDTO;
import com.carehub.nurses.model.UpdateNurseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class NurseService {

    @Autowired
    private NurseJdbcRepository nurseRepository;

    @Autowired
    private UserJdbcRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public NurseDTO createNurse(CreateNurseDTO body) {
        var entity = new Nurse()
                .setName(body.getName())
                .setCoren(body.getCoren());

        var user = new User();
        user.setUsername(body.getLogin());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setRoles(java.util.List.of("ROLE_NURSE"));
        var userCreated = userRepository.save(user);

        entity.setUserId(userCreated.getId());
        var saved = nurseRepository.save(entity);
        return NurseMapper.toDTO(saved);
    }

    public NurseDTO findById(Long nurseId) {
        var entity = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse not found"));
        return NurseMapper.toDTO(entity);
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
        dto.setItems(items.stream().map(NurseMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public NurseDTO updateNurse(Long nurseId, UpdateNurseDTO body) {
        var entity = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse not found"));

        if (body.getName() != null) entity.setName(body.getName());
        if (body.getCoren() != null) entity.setCoren(body.getCoren());

        nurseRepository.update(entity);
        return NurseMapper.toDTO(entity);
    }

    public void deleteNurse(Long nurseId) {
        nurseRepository.deleteById(nurseId);
    }

}
