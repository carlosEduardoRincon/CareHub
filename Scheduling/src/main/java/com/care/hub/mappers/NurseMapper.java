package com.care.hub.mappers;

import com.care.hub.data.entities.Nurse;
import com.carehub.nurses.model.NurseDTO;

public final class NurseMapper {
    private NurseMapper() {
    }

    public static NurseDTO toDTO(Nurse entity) {
        if (entity == null) return null;

        var nurseDTO = new NurseDTO();
        nurseDTO.setId(entity.getId());
        nurseDTO.setName(entity.getName());
        nurseDTO.setCoren(entity.getCoren());

        return nurseDTO;
    }
}
