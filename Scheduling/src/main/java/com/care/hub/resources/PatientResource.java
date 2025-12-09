package com.care.hub.resources;

import com.care.hub.services.PatientService;
import com.carehub.patients.api.PatientsApi;
import com.carehub.patients.model.CreatePatientDTO;
import com.carehub.patients.model.PatientDTO;
import com.carehub.patients.model.PaginatedPatientsDTO;
import com.carehub.patients.model.UpdatePatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class PatientResource implements PatientsApi {

    @Autowired
    private PatientService patientService;

    @Override
    public ResponseEntity<PatientDTO> createPatient(CreatePatientDTO body) {
        log.info("[POST] - Create Patient");
        var created = this.patientService.createPatient(body);
        return ResponseEntity
                .created(URI.create("/patient/" + created.getId()))
                .body(created);
    }

    @Override
    public ResponseEntity<PatientDTO> getPatient(Long patientId) {
        log.info("[GET] - Get Patient with ID: {}", patientId);
        var patient = this.patientService.findById(patientId);
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<PaginatedPatientsDTO> listPatients(Integer page, Integer perPage) {
        log.info("[GET] - List Patients - page: {}, perPage: {}", page, perPage);
        var paginated = this.patientService.listPatients(page, perPage);
        return ResponseEntity.ok(paginated);
    }

    @Override
    public ResponseEntity<PatientDTO> updatePatient(Long patientId, UpdatePatientDTO body) {
        log.info("[PUT] - Update Patient with ID: {}", patientId);
        var updated = this.patientService.updatePatient(patientId, body);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deletePatient(Long patientId) {
        log.info("[DELETE] - Remove Patient with ID: {}", patientId);
        this.patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}
