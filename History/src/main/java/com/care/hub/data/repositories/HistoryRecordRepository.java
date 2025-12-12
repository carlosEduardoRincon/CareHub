package com.care.hub.data.repositories;

import com.care.hub.data.entities.HistoryRecord;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Long> {

    List<HistoryRecord> findByPatientIdOrderByEventTimeAsc(String patientId);

    List<HistoryRecord> findByPatientIdAndEventTimeAfterOrderByEventTimeAsc(String patientId, Instant eventTimeAfter);
}
