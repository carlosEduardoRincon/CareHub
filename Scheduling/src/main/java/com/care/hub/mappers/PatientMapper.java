package com.care.hub.mappers;

import com.care.hub.data.entities.Patient;
import com.carehub.patients.model.PatientDTO;

public final class PatientMapper {
    private PatientMapper() {
    }

    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) return null;
        var dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setBirthDate(patient.getBirthDate());
        dto.setAddress(patient.getAddress());
        dto.setTelephone(patient.getTelephone());
        return dto;
    }
}
