package com.care.hub.mappers;

import com.care.hub.data.entities.Nurse;
import com.carehub.nurses.model.NurseDTO;

public final class NurseMapper {
    private NurseMapper() {
    }

    public static NurseDTO toDTO(Nurse entity) {
        if (entity == null) return null;
        var dto = new NurseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLogin(entity.getLogin());
        dto.setCpf(entity.getCpf());
        dto.setCoren(entity.getCoren());
        return dto;
    }
}
