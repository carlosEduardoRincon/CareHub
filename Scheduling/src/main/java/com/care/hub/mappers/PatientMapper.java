package com.care.hub.mappers;

import com.care.hub.data.entities.Patient;
import com.carehub.patients.model.PatientDTO;

public final class PatientMapper {
    private PatientMapper() {
    }

    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) return null;

        var patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setBirthDate(patient.getBirthDate());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setPassword(patient.getPassword());
        patientDTO.setTelephone(patient.getTelephone());

        return patientDTO;
    }
}
