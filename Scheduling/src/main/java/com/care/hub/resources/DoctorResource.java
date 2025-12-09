package com.care.hub.resources;

import com.care.hub.services.DoctorService;
import com.carehub.doctors.api.DoctorsApi;
import com.carehub.doctors.model.CreateDoctorDTO;
import com.carehub.doctors.model.DoctorDTO;
import com.carehub.doctors.model.PaginatedDoctorsDTO;
import com.carehub.doctors.model.UpdateDoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class DoctorResource implements DoctorsApi {

    @Autowired
    private DoctorService doctorService;

    @Override
    public ResponseEntity<DoctorDTO> createDoctor(CreateDoctorDTO body) {
        log.info("[POST] - Create Doctor");
        var created = this.doctorService.createDoctor(body);
        return ResponseEntity
                .created(URI.create("/doctor/" + created.getId()))
                .body(created);
    }

    @Override
    public ResponseEntity<DoctorDTO> getDoctor(Long doctorId) {
        log.info("[GET] - Get Doctor with ID: {}", doctorId);
        var doctor = this.doctorService.findById(doctorId);
        return ResponseEntity.ok(doctor);
    }

    @Override
    public ResponseEntity<PaginatedDoctorsDTO> listDoctors(Integer page, Integer perPage) {
        log.info("[GET] - List Doctors - page: {}, perPage: {}", page, perPage);
        var paginated = this.doctorService.listDoctors(page, perPage);
        return ResponseEntity.ok(paginated);
    }

    @Override
    public ResponseEntity<DoctorDTO> updateDoctor(Long doctorId, UpdateDoctorDTO body) {
        log.info("[PUT] - Update Doctor with ID: {}", doctorId);
        var updated = this.doctorService.updateDoctor(doctorId, body);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteDoctor(Long doctorId) {
        log.info("[DELETE] - Remove Doctor with ID: {}", doctorId);
        this.doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

}
