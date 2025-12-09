package com.care.hub.mappers;

import com.care.hub.data.entities.Doctor;
import com.carehub.doctors.model.DoctorDTO;

public final class DoctorMapper {
    private DoctorMapper() {
    }

    public static DoctorDTO toDTO(Doctor entity) {
        if (entity == null) return null;
        var dto = new DoctorDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setCrm(entity.getCrm());
        dto.setSpeciality(DoctorDTO.SpecialityEnum.fromValue(entity.getSpeciality()));
        return dto;
    }
}
