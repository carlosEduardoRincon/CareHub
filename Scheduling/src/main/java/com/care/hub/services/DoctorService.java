package com.care.hub.services;

import com.care.hub.data.entities.Doctor;
import com.care.hub.data.repositories.DoctorJdbcRepository;
import com.care.hub.data.repositories.UserJdbcRepository;
import com.care.hub.mappers.DoctorMapper;
import com.carehub.doctors.model.CreateDoctorDTO;
import com.carehub.doctors.model.DoctorDTO;
import com.carehub.doctors.model.PaginatedDoctorsDTO;
import com.carehub.doctors.model.UpdateDoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorJdbcRepository doctorRepository;

    @Autowired
    private UserJdbcRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DoctorDTO createDoctor(CreateDoctorDTO body) {
        var entity = new Doctor()
                .setName(body.getName())
                .setEmail(body.getEmail())
                .setLogin(body.getLogin())
                .setPassword(body.getPassword())
                .setCpf(body.getCpf())
                .setCrm(body.getCrm())
                .setSpeciality(body.getSpeciality().toString());

        var user = new com.care.hub.data.entities.User();
        user.setUsername(body.getLogin());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setRoles(java.util.List.of("ROLE_DOCTOR"));
        userRepository.save(user);

        var saved = doctorRepository.save(entity);
        return DoctorMapper.toDTO(saved);
    }

    public DoctorDTO findById(Long doctorId) {
        var entity = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
        return DoctorMapper.toDTO(entity);
    }

    public PaginatedDoctorsDTO listDoctors(Integer page, Integer perPage) {
        int p = page != null ? page : 0;
        int pp = perPage != null ? perPage : 10;

        var items = doctorRepository.findAll(p, pp);
        var total = doctorRepository.count();

        var dto = new PaginatedDoctorsDTO();
        dto.setPage(p);
        dto.setPerPage(pp);
        dto.setTotal((long) total);
        dto.setItems(items.stream().map(DoctorMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public DoctorDTO updateDoctor(Long doctorId, UpdateDoctorDTO body) {
        var entity = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        if (body.getName() != null) entity.setName(body.getName());
        if (body.getEmail() != null) entity.setEmail(body.getEmail());
        if (body.getSpeciality() != null) entity.setSpeciality(body.getSpeciality().toString());

        doctorRepository.update(entity);
        return DoctorMapper.toDTO(entity);
    }

    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }
}
