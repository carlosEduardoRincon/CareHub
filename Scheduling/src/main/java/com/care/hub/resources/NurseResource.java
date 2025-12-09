package com.care.hub.resources;

import com.care.hub.services.NurseService;
import com.carehub.nurses.api.NursesApi;
import com.carehub.nurses.model.CreateNurseDTO;
import com.carehub.nurses.model.NurseDTO;
import com.carehub.nurses.model.PaginatedNursesDTO;
import com.carehub.nurses.model.UpdateNurseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class NurseResource implements NursesApi {

    @Autowired
    private NurseService nurseService;

    @Override
    public ResponseEntity<NurseDTO> createNurse(CreateNurseDTO body) {
        log.info("[POST] - Create Nurse");
        var created = this.nurseService.createNurse(body);
        return ResponseEntity
                .created(URI.create("/nurse/" + created.getId()))
                .body(created);
    }

    @Override
    public ResponseEntity<NurseDTO> getNurse(Long nurseId) {
        log.info("[GET] - Get Nurse with ID: {}", nurseId);
        var nurse = this.nurseService.findById(nurseId);
        return ResponseEntity.ok(nurse);
    }

    @Override
    public ResponseEntity<PaginatedNursesDTO> listNurses(Integer page, Integer perPage) {
        log.info("[GET] - List Nurses - page: {}, perPage: {}", page, perPage);
        var paginated = this.nurseService.listNurses(page, perPage);
        return ResponseEntity.ok(paginated);
    }

    @Override
    public ResponseEntity<NurseDTO> updateNurse(Long nurseId, UpdateNurseDTO body) {
        log.info("[PUT] - Update Nurse with ID: {}", nurseId);
        var updated = this.nurseService.updateNurse(nurseId, body);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteNurse(Long nurseId) {
        log.info("[DELETE] - Remove Nurse with ID: {}", nurseId);
        this.nurseService.deleteNurse(nurseId);
        return ResponseEntity.noContent().build();
    }
}
