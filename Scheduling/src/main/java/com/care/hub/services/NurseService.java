package com.care.hub.services;

import com.carehub.nurses.model.CreateNurseDTO;
import com.carehub.nurses.model.NurseDTO;
import com.carehub.nurses.model.PaginatedNursesDTO;
import com.carehub.nurses.model.UpdateNurseDTO;
import org.springframework.stereotype.Service;

@Service
public class NurseService {
    public NurseDTO createNurse(CreateNurseDTO body) {
        return null;
    }

    public NurseDTO findById(Long nurseId) {
        return null;
    }

    public PaginatedNursesDTO listNurses(Integer page, Integer perPage) {
        return null;
    }

    public NurseDTO updateNurse(Long nurseId, UpdateNurseDTO body) {
        return null;
    }

    public void deleteNurse(Long nurseId) {
    }
}
