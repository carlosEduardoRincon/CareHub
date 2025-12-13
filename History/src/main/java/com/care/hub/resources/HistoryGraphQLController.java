package com.care.hub.resources;

import com.care.hub.dtos.GraphQLHistoryRecordDTO;
import com.care.hub.data.entities.HistoryRecord;
import com.care.hub.data.repositories.HistoryRecordRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HistoryGraphQLController {

    private final HistoryRecordRepository repository;

    public HistoryGraphQLController(HistoryRecordRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<GraphQLHistoryRecordDTO> historyByPatient(@Argument Long patientId, @Argument Boolean onlyFuture) {
        boolean future = Boolean.TRUE.equals(onlyFuture);
        var records = future
                ? repository.findByPatientIdAndEventTimeAfterOrderByEventTimeAsc(patientId, Instant.now())
                : repository.findByPatientIdOrderByEventTimeAsc(patientId);

        return records.stream()
                .map(this::toGraphQLDTO)
                .collect(Collectors.toList());
    }

    private GraphQLHistoryRecordDTO toGraphQLDTO(HistoryRecord historyRecord) {
        String eventTime = historyRecord.getEventTime() == null ? null : historyRecord.getEventTime().toString();
        String createdAt = historyRecord.getCreatedAt() == null ? null : historyRecord.getCreatedAt().toString();
        return new GraphQLHistoryRecordDTO(
                historyRecord.getId(),
                historyRecord.getPatientId(),
                historyRecord.getScheduleId(),
                historyRecord.getDoctorId(),
                historyRecord.getEventType(),
                eventTime,
                historyRecord.getPayload(),
                createdAt
        );
    }
}
