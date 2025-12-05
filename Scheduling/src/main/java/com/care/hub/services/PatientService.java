package com.care.hub.services;

import com.carehub.patients.model.CreatePatientDTO;
import com.carehub.patients.model.PatientDTO;
import com.carehub.patients.model.PaginatedPatientsDTO;
import com.carehub.patients.model.UpdatePatientDTO;
import org.springframework.stereotype.Service;
import com.care.hub.data.entities.Patient;
import com.care.hub.data.repositories.PatientJdbcRepository;
import com.care.hub.mappers.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientJdbcRepository patientRepository;

    @Autowired
    private com.care.hub.data.repositories.UserJdbcRepository userRepository;

    public PatientDTO createPatient(CreatePatientDTO body) {
        var patient = new Patient();
        patient.setName(body.getName());
        patient.setEmail(body.getEmail());
        patient.setBirthDate(body.getBirthDate());
        patient.setAddress(body.getAddress());
        patient.setTelephone(body.getTelephone());

        var user = new com.care.hub.data.entities.User();
        user.setUsername(body.getEmail());
        user.setPassword(java.util.UUID.randomUUID().toString());
        user.setRoles(java.util.List.of("ROLE_PATIENT"));
        userRepository.save(user);
        patient.setUserId(user.getId());

        var savedId = patientRepository.save(patient);
        patient.setId(savedId.getId());

        return PatientMapper.toDTO(patient);
    }

    public PatientDTO findById(Long patientId) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        return PatientMapper.toDTO(patient);
    }

    public PaginatedPatientsDTO listPatients(Integer page, Integer perPage) {
        var patients = patientRepository.findAll(page, perPage);
        var total = patientRepository.count();

        var dto = new PaginatedPatientsDTO();
        dto.setPage(page);
        dto.setPerPage(perPage);
        dto.setTotal((long) total);
        dto.setItems(patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public PatientDTO updatePatient(Long patientId, UpdatePatientDTO body) {
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patient.setName(body.getName());
        patient.setEmail(body.getEmail());
        patient.setAddress(body.getAddress());
        patient.setTelephone(body.getTelephone());

        patientRepository.update(patient);
        return PatientMapper.toDTO(patient);
    }

    public void deletePatient(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
